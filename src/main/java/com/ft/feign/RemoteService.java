package com.ft.feign;

import com.ft.model.mdo.UserDO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * 远程service
 *
 * @author shichunyang
 */
@FeignClient(name = "feign", fallbackFactory = RemoteServiceFallbackFactory.class)
public interface RemoteService {

	/**
	 * json body
	 *
	 * @param userDO 用户model
	 * @return 服务端返回结果
	 */
	@PutMapping("/json/user")
	String user(
			@RequestBody UserDO userDO
	);

	/**
	 * get方法测试
	 *
	 * @param username 用户名
	 * @param age      年龄
	 * @return 服务返回结果
	 */
	@RequestMapping(value = "/get", method = RequestMethod.GET)
	String get(
			@RequestParam(value = "username", required = false) String username,
			@RequestParam(value = "age") int age
	);
}
