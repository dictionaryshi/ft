package com.ft.br.controller;

import com.ft.br.model.ao.LoginAO;
import com.ft.br.model.bo.CodeBO;
import com.ft.br.model.bo.TokenBO;
import com.ft.br.service.SsoService;
import com.ft.util.model.RestResult;
import com.ft.web.model.UserBO;
import com.ft.web.util.CookieUtil;
import com.ft.web.util.WebUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * 登录相关api
 *
 * @author shichunyang
 */
@Api(tags = "SSO API")
@RestController
@Slf4j
@CrossOrigin
@RequestMapping(RestResult.API + "/sso")
public class LoginRestController {

	@Value("${cookieDomain}")
	private String cookieDomain;

	@Autowired
	private SsoService ssoService;

	/**
	 * 图片验证码
	 */
	@ApiOperation("获取图片验证码")
	@GetMapping("/code")
	public RestResult<CodeBO> code() {
		CodeBO codeBO = ssoService.getCode();
		return RestResult.success(codeBO);
	}

	/**
	 * 登录
	 */
	@ApiOperation("用户登录")
	@PostMapping("/login")
	public RestResult<TokenBO> login(
			@Valid LoginAO loginAO,
			HttpServletResponse response
	) {
		TokenBO tokenBO = ssoService.login(loginAO);

		String domain = this.cookieDomain;
		CookieUtil.addCookie(response, WebUtil.COOKIE_LOGIN, tokenBO.getToken(), CookieUtil.MAX_AGE_BROWSER, domain, true);

		return RestResult.success(tokenBO);
	}

	/**
	 * 查询当前登录用户信息
	 *
	 * @return 当前登录用户信息
	 */
	@ApiOperation("查询当前登录用户信息")
	@GetMapping("/current-user")
	public RestResult<UserBO> user(
			HttpServletRequest request
	) {
		return null;
	}
}
