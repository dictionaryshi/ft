package com.ft.br.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.ft.br.constant.LoginConstant;
import com.ft.br.dao.UserMapper;
import com.ft.br.model.ao.CurrentUserAO;
import com.ft.br.model.ao.LoginAO;
import com.ft.br.model.bo.CodeBO;
import com.ft.br.model.bo.TokenBO;
import com.ft.br.service.SsoService;
import com.ft.dao.stock.model.UserDO;
import com.ft.db.constant.DbConstant;
import com.ft.redis.base.ValueOperationsCache;
import com.ft.util.*;
import com.ft.util.exception.FtException;
import com.ft.util.model.LogAO;
import com.ft.util.model.LogBO;
import com.ft.web.model.UserBO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.concurrent.TimeUnit;

/**
 * SsoServiceImpl
 *
 * @author shichunyang
 */
@Slf4j
@Service(value = "com.ft.br.service.impl.SsoServiceImpl")
public class SsoServiceImpl implements SsoService {

	@Autowired
	private ValueOperationsCache<String, String> valueOperationsCache;

	@Autowired
	private UserMapper userMapper;

	@Override
	@Transactional(value = DbConstant.DB_CONSIGN + DbConstant.TRAN_SACTION_MANAGER, propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
	public void deadLock(int lockId1, int lockId2) {
		userMapper.deadLock(lockId1);
		userMapper.deadLock(lockId2);
	}

	@Override
	public CodeBO getCode() {
		CodeBO codeBO = new CodeBO();

		String codeId = CommonUtil.get32UUID();
		codeBO.setCodeId(codeId);

		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		String code = ImageUtil.getImage(byteArrayOutputStream);
		String img = Base64.getEncoder().encodeToString(byteArrayOutputStream.toByteArray());
		codeBO.setImg(img);

		// code codeId做关系绑定
		String codeIdKey = StringUtil.append(StringUtil.REDIS_SPLIT, LoginConstant.REDIS_VERIFICATION_CODE, codeId);
		valueOperationsCache.setIfAbsent(codeIdKey, code, 300_000L, TimeUnit.MILLISECONDS);

		return codeBO;
	}

	@Override
	public TokenBO login(LoginAO loginAO) {
		// 校验code
		String errorMessage = this.checkCode(loginAO);
		if (errorMessage != null) {
			FtException.throwException(errorMessage);
		}

		// 校验用户名 密码
		UserBO userBO = this.checkUserAndPassword(loginAO);

		// token 用户信息绑定
		String token = CommonUtil.get32UUID();
		String tokenKey = StringUtil.append(StringUtil.REDIS_SPLIT, LoginConstant.REDIS_LOGIN_TOKEN, token);
		valueOperationsCache.setIfAbsent(tokenKey, JsonUtil.object2Json(userBO), 3600_000L, TimeUnit.MILLISECONDS);

		TokenBO tokenBO = new TokenBO();
		tokenBO.setToken(token);
		tokenBO.setUser(userBO);

		return tokenBO;
	}

	private UserBO checkUserAndPassword(LoginAO loginAO) {
		UserDO userDO = userMapper.getUserByUserName(loginAO.getUsername());
		if (userDO == null) {
			LogBO logBO = LogUtil.log("用户名不存在",
					LogAO.build("username", loginAO.getUsername()));
			log.info(logBO.getLogPattern(), logBO.getParams());
			FtException.throwException("用户名或密码不正确");
		}

		String md5Password = EncodeUtil.md5Encode(loginAO.getPassword());
		if (!ObjectUtil.equals(md5Password, userDO.getPassword())) {
			FtException.throwException("用户名或密码不正确");
		}

		UserBO userBO = new UserBO();
		userBO.setId(userDO.getId());
		userBO.setUsername(userDO.getUsername());
		userBO.setCreatedAt(userDO.getCreatedAt());
		userBO.setUpdatedAt(userDO.getUpdatedAt());

		return userBO;
	}

	private String checkCode(LoginAO loginAO) {
		String errorMessage = "验证码不正确";

		// 校验code
		String codeId = loginAO.getCodeId();
		String redisCode = valueOperationsCache.get(StringUtil.append(StringUtil.REDIS_SPLIT, LoginConstant.REDIS_VERIFICATION_CODE, codeId));
		if (StringUtil.isNull(redisCode)) {
			LogBO logBO = LogUtil.log("验证码不存在或已过期",
					LogAO.build("codeId", codeId));
			log.info(logBO.getLogPattern(), logBO.getParams());
			return errorMessage;
		}

		if (!redisCode.equalsIgnoreCase(loginAO.getCode())) {
			LogBO logBO = LogUtil.log("验证码比对不一致",
					LogAO.build("code", loginAO.getCode()),
					LogAO.build("redisCode", redisCode));
			log.info(logBO.getLogPattern(), logBO.getParams());
			return errorMessage;
		}

		return null;
	}

	@Override
	public UserBO currentUser(CurrentUserAO currentUserAO) {
		String tokenKey = StringUtil.append(StringUtil.REDIS_SPLIT, LoginConstant.REDIS_LOGIN_TOKEN, currentUserAO.getToken());
		String userJson = valueOperationsCache.get(tokenKey);

		if (StringUtil.isNull(userJson)) {
			return null;
		}

		valueOperationsCache.expire(tokenKey, 3600_000L, TimeUnit.MILLISECONDS);

		return JsonUtil.json2Object(userJson, new TypeReference<UserBO>() {
		});
	}
}
