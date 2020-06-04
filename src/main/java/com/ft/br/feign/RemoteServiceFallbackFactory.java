package com.ft.br.feign;

import com.ft.rpc.api.model.RpcParam;
import com.ft.rpc.api.model.RpcResult;
import com.ft.util.JsonUtil;
import com.ft.util.LogUtil;
import com.ft.util.constant.CommonResponseConstant;
import com.ft.util.model.RestResult;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * RemoteServiceFallbackFactory
 *
 * @author shichunyang
 */
@Slf4j
@Component
public class RemoteServiceFallbackFactory implements FallbackFactory<RemoteService> {

    @Override
    public RemoteService create(Throwable throwable) {
        return new RemoteService() {
            @Override
            public RestResult<RpcResult> put(RpcParam rpcParam) {
                log.info(LogUtil.build("put down", throwable,
                        "params", JsonUtil.object2Json(rpcParam)));

                return RestResult.fail(CommonResponseConstant.ERROR_CODE.getCode(), LogUtil.build("put down", throwable,
                        "params", JsonUtil.object2Json(rpcParam)));
            }

            @Override
            public RestResult<RpcResult> get(RpcParam rpcParam) {
                log.info(LogUtil.build("get down", throwable,
                        "params", JsonUtil.object2Json(rpcParam)));

                return RestResult.fail(CommonResponseConstant.ERROR_CODE.getCode(), LogUtil.build("get down", throwable,
                        "params", JsonUtil.object2Json(rpcParam)));
            }
        };
    }
}
