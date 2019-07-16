package com.ft.br.util;

import com.ft.br.constant.LoginConstant;
import com.ft.redis.base.ValueOperationsCache;
import com.ft.util.JsonUtil;
import com.ft.util.SpringContextUtil;
import com.ft.util.StringUtil;
import com.ft.util.exception.FtException;
import com.ft.web.model.UserDO;
import com.ft.web.util.WebUtil;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.TimeUnit;

/**
 * 登录工具类
 *
 * @author shichunyang
 */
@Slf4j
public class LoginUtil {

	/**
	 * 获取登录用户信息
	 *
	 * @param request 请求对象
	 * @return 用户信息
	 */
	public static UserDO getLoginUser(HttpServletRequest request) {
		@SuppressWarnings("unchecked")
		ValueOperationsCache<String, String> valueOperationsCache = SpringContextUtil.getBean(ValueOperationsCache.class);

		String loginToken = WebUtil.getToken(request);
		if (StringUtil.isNull(loginToken)) {
			log.info("url==>{}, login_token 不存在", request.getRequestURL());
			FtException.throwException("签名错误");
		}

		String redisTokenKey = StringUtil.append(StringUtil.REDIS_SPLIT, LoginConstant.REDIS_LOGIN_TOKEN, loginToken);
		String userJson = valueOperationsCache.get(redisTokenKey);

		if (StringUtil.isNull(userJson)) {
			log.info("url==>{}, login_token 不存在或已过期", request.getRequestURL());
			FtException.throwException("签名错误");
		}

		boolean flag = valueOperationsCache.expire(redisTokenKey, 3600_000L, TimeUnit.MILLISECONDS);
		log.info("用户token续时==>{}", flag);

		return JsonUtil.json2Object(userJson, UserDO.class);
	}
}
