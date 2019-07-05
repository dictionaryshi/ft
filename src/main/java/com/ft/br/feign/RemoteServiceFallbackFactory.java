package com.ft.br.feign;

import com.ft.rpc.api.model.RpcParam;
import com.ft.rpc.api.model.RpcResult;
import com.ft.util.JsonUtil;
import com.ft.util.constant.CommonResponseConstant;
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
				return RestResult.fail(CommonResponseConstant.ERROR_CODE.getCode(), JsonUtil.object2Json(rpcParam) + "_put_down");
			}

			@Override
			public RestResult<RpcResult> get(String username, Integer age) {
				return RestResult.fail(CommonResponseConstant.ERROR_CODE.getCode(), username + "_" + age + "_get_down");
			}
		};
	}
}
