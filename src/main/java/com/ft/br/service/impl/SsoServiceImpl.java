package com.ft.br.service.impl;

import com.ft.br.constant.LoginConstant;
import com.ft.br.model.ao.LoginAO;
import com.ft.br.model.bo.CodeBO;
import com.ft.br.model.bo.TokenBO;
import com.ft.br.service.SsoService;
import com.ft.redis.base.ValueOperationsCache;
import com.ft.util.CommonUtil;
import com.ft.util.ImageUtil;
import com.ft.util.LogUtil;
import com.ft.util.StringUtil;
import com.ft.util.exception.FtException;
import com.ft.util.model.LogAO;
import com.ft.util.model.LogBO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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

	@Value("${cookieDomain}")
	private String cookieDomain;

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
		return null;
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
}
