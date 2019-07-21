package com.ft.br.controller;

import com.ft.br.constant.LoginConstant;
import com.ft.br.model.bo.CodeBO;
import com.ft.br.service.LoginService;
import com.ft.br.service.SsoService;
import com.ft.br.util.LoginUtil;
import com.ft.util.JsonUtil;
import com.ft.util.model.RestResult;
import com.ft.web.constant.SwaggerConstant;
import com.ft.web.model.UserBO;
import com.ft.web.util.CookieUtil;
import com.ft.web.util.WebUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

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

	@Autowired
	private LoginService loginService;

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

	@ApiOperation("登录")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "username", value = "用户名", required = true, dataType = SwaggerConstant.DATA_TYPE_STRING, paramType = SwaggerConstant.PARAM_TYPE_QUERY),
			@ApiImplicitParam(name = "password", value = "密码", required = true, dataType = SwaggerConstant.DATA_TYPE_STRING, paramType = SwaggerConstant.PARAM_TYPE_QUERY),
			@ApiImplicitParam(name = "code", value = "验证码", required = true, dataType = SwaggerConstant.DATA_TYPE_STRING, paramType = SwaggerConstant.PARAM_TYPE_QUERY),
			@ApiImplicitParam(name = "code_id", value = "验证码绑定id", required = true, dataType = SwaggerConstant.DATA_TYPE_STRING, paramType = SwaggerConstant.PARAM_TYPE_QUERY),
	})
	/**
	 * 登录
	 *
	 * @param username 用户名
	 * @param password 密码
	 * @param code     验证码
	 * @return token
	 */
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String login(
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "username") String username,
			@RequestParam(value = "password") String password,
			@RequestParam(value = "code") String code,
			@RequestParam(value = "code_id") String codeId
	) {
		username = username.trim();
		password = password.trim();
		code = code.trim();
		codeId = codeId.trim();

		String token = loginService.getLoginToken(username, password, code, codeId);

		request.setAttribute(WebUtil.PARAM_LOGIN_TOKEN, token);

		Map<String, Object> result = new HashMap<>(16);
		result.put(LoginConstant.RETURN_TOKEN, token);
		result.put(LoginConstant.PARAM_LOGIN_USER, LoginUtil.getLoginUser(request));

		String domain = this.cookieDomain;
		CookieUtil.addCookie(response, WebUtil.PARAM_LOGIN_TOKEN, token, CookieUtil.MAX_AGE_BROWSER, domain, true);

		return JsonUtil.object2Json(RestResult.success(result));
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
