package com.ft.feign;

import com.ft.rpc.api.model.RpcParam;
import com.ft.rpc.api.model.RpcResult;
import com.ft.util.JsonUtil;
import com.ft.util.model.RestResult;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * RemoteServiceFallbackFactory
 *
 * @author shichunyang
 */
@Component
public class RemoteServiceFallbackFactory implements FallbackFactory<RemoteService> {
	@Override
	public RemoteService create(Throwable throwable) {
		return new RemoteService() {
			@Override
			public RestResult<RpcResult> put(RpcParam rpcParam) {
				return RestResult.getErrorRestResult(JsonUtil.object2Json(rpcParam) + "_put_down", null);
			}

			@Override
			public RestResult<RpcResult> get(String username, Integer age) {
				return RestResult.getErrorRestResult(username + "_" + age + "_get_down", null);
			}
		};
	}
}
