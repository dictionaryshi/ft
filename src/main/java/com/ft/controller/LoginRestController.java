package com.ft.controller;

import com.ft.annotation.LoginCheck;
import com.ft.constant.LoginConstant;
import com.ft.model.dto.UserDTO;
import com.ft.redis.base.ValueOperationsCache;
import com.ft.service.LoginService;
import com.ft.util.*;
import com.ft.web.constant.SwaggerConstant;
import com.ft.web.exception.FtException;
import com.ft.web.model.RestResult;
import com.ft.web.util.CookieUtil;
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
import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * 登录相关api
 *
 * @author shichunyang
 */
@Api(tags = "登陆API")
@RestController
@Slf4j
@CrossOrigin
@RequestMapping(RestResult.API)
public class LoginRestController {

	@Autowired
	private LoginService loginService;

	@Autowired
	private ValueOperationsCache valueOperationsCache;

	@Value("${cookieDomain}")
	private String cookieDomain;

	@ApiOperation(value = "登录")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "username", value = "用户名", required = true, dataType = SwaggerConstant.DATA_TYPE_STRING, paramType = SwaggerConstant.PARAM_TYPE_FORM),
			@ApiImplicitParam(name = "password", value = "密码", required = true, dataType = SwaggerConstant.DATA_TYPE_STRING, paramType = SwaggerConstant.PARAM_TYPE_FORM),
			@ApiImplicitParam(name = "code", value = "验证码", required = true, dataType = SwaggerConstant.DATA_TYPE_STRING, paramType = SwaggerConstant.PARAM_TYPE_FORM),
			@ApiImplicitParam(name = "code_id", value = "验证码绑定id", required = true, dataType = SwaggerConstant.DATA_TYPE_STRING, paramType = SwaggerConstant.PARAM_TYPE_FORM),
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

		request.setAttribute(LoginConstant.PARAM_LOGIN_TOKEN, token);

		Map<String, Object> result = new HashMap<>(16);
		result.put(LoginConstant.RETURN_TOKEN, token);
		result.put(LoginConstant.PARAM_LOGIN_USER, LoginUtil.getLoginUser(request));

		String domain = this.cookieDomain;
		CookieUtil.addCookie(response, LoginConstant.PARAM_LOGIN_TOKEN, token, CookieUtil.MAX_AGE_BROWSER, domain, true);

		return JsonUtil.object2Json(RestResult.getSuccessRestResult(result));
	}

	@ApiOperation(value = "图片验证码")
	/**
	 * 图片验证码
	 */
	@RequestMapping(value = "/code", method = RequestMethod.POST)
	public String code() {
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		String code = ImageUtil.getImage(byteArrayOutputStream);
		String codeId = CommonUtil.get32UUID();

		String codeRedisKey = StringUtil.append(StringUtil.REDIS_SPLIT, LoginConstant.REDIS_VERIFICATION_CODE, codeId);
		boolean flag = valueOperationsCache.setNX(codeRedisKey, code, 300_000L);
		if (!flag) {
			throw new FtException(RestResult.ERROR_CODE, "验证码存储异常");
		}

		Map<String, Object> result = new HashMap<>(16);
		result.put("code_id", codeId);
		result.put("img", Base64.getEncoder().encodeToString(byteArrayOutputStream.toByteArray()));
		return JsonUtil.object2Json(RestResult.getSuccessRestResult(result));
	}

	@ApiOperation(value = "查询当前登录用户信息")
	/**
	 * 查询当前登录用户信息
	 *
	 * @return 当前登录用户信息
	 */
	@RequestMapping(value = "/user", method = RequestMethod.POST)
	@LoginCheck
	public String user(HttpServletRequest request) {
		return JsonUtil.object2Json(RestResult.getSuccessRestResult(LoginUtil.getLoginUser(request)));
	}

	@RequestMapping(value = "/json-param", method = RequestMethod.POST)
	@LoginCheck
	public String jsonParam(@RequestBody UserDTO body) {
		return JsonUtil.object2Json(body);
	}
}
