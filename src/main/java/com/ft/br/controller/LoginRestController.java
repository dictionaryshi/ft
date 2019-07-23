package com.ft.br.controller;

import com.ft.br.constant.RedisKey;
import com.ft.br.model.ao.sso.CurrentUserAO;
import com.ft.br.model.ao.sso.LoginAO;
import com.ft.br.model.bo.CodeBO;
import com.ft.br.model.bo.TokenBO;
import com.ft.br.service.SsoService;
import com.ft.redis.lock.RedisLock;
import com.ft.redis.util.RedisUtil;
import com.ft.util.StringUtil;
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

	@Autowired
	private RedisLock redisLock;

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
			@RequestBody @Valid LoginAO loginAO,
			HttpServletRequest request,
			HttpServletResponse response
	) {
		String lockKey = RedisUtil.getRedisKey(RedisKey.REDIS_SSO_LOGIN_LOCK, loginAO.getUsername());
		try {
			redisLock.lock(lockKey, 10_000L);
			String token = WebUtil.getToken(request);
			if (!StringUtil.isNull(token)) {
				CurrentUserAO currentUserAO = new CurrentUserAO();
				currentUserAO.setToken(token);

				UserBO userBO = ssoService.currentUser(currentUserAO);
				if (userBO != null) {
					TokenBO tokenBO = new TokenBO();
					tokenBO.setUser(userBO);
					return RestResult.success(tokenBO);
				}
			}

			TokenBO tokenBO = ssoService.login(loginAO);

			String domain = this.cookieDomain;
			CookieUtil.addCookie(response, WebUtil.COOKIE_LOGIN, tokenBO.getToken(), CookieUtil.MAX_AGE_BROWSER, domain, true);

			return RestResult.success(tokenBO);
		} finally {
			redisLock.unlock(lockKey);
		}
	}

	/**
	 * 查询当前登录用户信息
	 *
	 * @return 当前登录用户信息
	 */
	@ApiOperation("查询当前登录用户信息")
	@GetMapping("/current-user")
	public RestResult<UserBO> user(
			@Valid CurrentUserAO currentUserAO
	) {
		UserBO userBO = ssoService.currentUser(currentUserAO);
		return RestResult.success(userBO);
	}
}
