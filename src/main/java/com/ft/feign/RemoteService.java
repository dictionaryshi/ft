package com.ft.feign;

import com.ft.model.mdo.UserDO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

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
}
