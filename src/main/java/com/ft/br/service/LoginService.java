package com.ft.br.service;

import com.ft.br.constant.LoginConstant;
import com.ft.br.dao.UserMapper;
import com.ft.redis.base.ValueOperationsCache;
import com.ft.util.CommonUtil;
import com.ft.util.EncodeUtil;
import com.ft.util.JsonUtil;
import com.ft.util.StringUtil;
import com.ft.util.exception.FtException;
import com.ft.util.model.RestResult;
import com.ft.web.model.UserDO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 登录业务逻辑
 *
 * @author shichunyang
 */
@Service
@Slf4j
public class LoginService {

	@Autowired
	private ValueOperationsCache valueOperationsCache;

	@Autowired
	private UserMapper userMapper;

	/**
	 * 获取登录token
	 *
	 * @param username 用户名
	 * @param password 密码
	 * @param code     图片验证码
	 * @return token
	 */
	public String getLoginToken(
			String username,
			String password,
			String code,
			String codeId
	) {
		// 校验code
		String redisCode = valueOperationsCache.get(StringUtil.append(StringUtil.REDIS_SPLIT, LoginConstant.REDIS_VERIFICATION_CODE, codeId));
		if (StringUtil.isNull(redisCode)) {
			log.info("redis code_id==>{}, 不存在或过期了", codeId);
			throw new FtException(RestResult.ERROR_CODE, "验证码不正确");
		}
		if (!redisCode.equals(code)) {
			log.info("redis code==>{}, 比对不正确", redisCode);
			throw new FtException(RestResult.ERROR_CODE, "验证码不正确");
		}

		// 校验用户名密码
		String md5Password = EncodeUtil.md5Encode(password);
		UserDO userDO = userMapper.getUserByUserNameAndPassword(username, md5Password);
		if (userDO == null) {
			throw new FtException(RestResult.ERROR_CODE, "用户名或密码不正确");
		}

		// 不把密码写到redis中
		userDO.setPassword(null);

		String token = CommonUtil.get32UUID();
		String redisTokenKey = StringUtil.append(StringUtil.REDIS_SPLIT, LoginConstant.REDIS_LOGIN_TOKEN, token);
		boolean flag = valueOperationsCache.setNX(redisTokenKey, JsonUtil.object2Json(userDO), 3600_000L);
		if (!flag) {
			throw new FtException(RestResult.ERROR_CODE, "redis_login_token 系统异常");
		}
		return token;
	}
}