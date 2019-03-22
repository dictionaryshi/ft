package com.ft.feign;

import com.ft.rpc.api.model.RpcParam;
import com.ft.rpc.api.model.RpcResult;
import com.ft.util.model.RestResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * 远程service
 *
 * @author shichunyang
 */
@FeignClient(name = "feign", fallbackFactory = RemoteServiceFallbackFactory.class)
@RequestMapping("/rpc")
public interface RemoteService {

	@PutMapping("/put")
	RestResult<RpcResult> put(
			@RequestBody RpcParam rpcParam
	);

	@RequestMapping(value = "/get", method = RequestMethod.GET)
	RestResult<RpcResult> get(
			@RequestParam(value = "username", required = false) String username,
			@RequestParam(value = "age") Integer age
	);
}
