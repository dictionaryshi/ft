package com.ft.br.feign;

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
@RequestMapping(RestResult.API + "/rpc")
public interface RemoteService {

	@PutMapping("/put")
	RestResult<RpcResult> put(
			@RequestBody RpcParam rpcParam
	);

	@GetMapping("/get")
	RestResult<RpcResult> get(
			RpcParam rpcParam
	);
}
