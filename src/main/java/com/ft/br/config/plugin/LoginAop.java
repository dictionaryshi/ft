package com.ft.br.config.plugin;

import com.ft.br.model.mdo.UserDO;
import com.ft.br.util.LoginUtil;
import com.ft.util.JsonUtil;
import com.ft.util.StringUtil;
import com.ft.web.util.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.annotation.Order;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * 登录校验token
 *
 * @author shichunyang
 */
@Order(0)
@Aspect
@Slf4j
public class LoginAop {

	@Before(value = "@annotation(com.ft.br.annotation.LoginCheck)")
	public void before() {

		// 获取request对象
		ServletRequestAttributes attributes =
				(ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = attributes.getRequest();
		String ip = HttpUtil.getIpAddress(request);

		String referer = request.getHeader("REFERER");
		String target = "swagger-ui.html";
		if (!StringUtil.isNull(referer) && referer.contains(target)) {
			log.info("ip==>{}, url==>{}, swagger request", ip, request.getRequestURL());
		}

		UserDO userDO = LoginUtil.getLoginUser(request);

		log.info("ip==>{}, url==>{}, login_token 验证通过, user==>{}", ip, request.getRequestURL(), JsonUtil.object2Json(userDO));
	}
}
