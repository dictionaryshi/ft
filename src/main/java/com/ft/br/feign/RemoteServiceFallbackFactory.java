package com.ft.br.feign;

import com.ft.rpc.api.model.RpcParam;
import com.ft.rpc.api.model.RpcResult;
import com.ft.util.JsonUtil;
import com.ft.util.LogUtil;
import com.ft.util.constant.CommonResponseConstant;
import com.ft.util.model.LogAO;
import com.ft.util.model.LogBO;
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
                LogBO logBO = LogUtil.log("put down", throwable,
                        LogAO.build("params", JsonUtil.object2Json(rpcParam)));
                log.info(logBO.getLogPattern(), logBO.getParams());

                return RestResult.fail(CommonResponseConstant.ERROR_CODE.getCode(), "put down",
                        LogAO.build("params", JsonUtil.object2Json(rpcParam)));
            }

            @Override
            public RestResult<RpcResult> get(RpcParam rpcParam) {
                LogBO logBO = LogUtil.log("get down", throwable,
                        LogAO.build("params", JsonUtil.object2Json(rpcParam)));
                log.info(logBO.getLogPattern(), logBO.getParams());

                return RestResult.fail(CommonResponseConstant.ERROR_CODE.getCode(), "get down",
                        LogAO.build("params", JsonUtil.object2Json(rpcParam)));
            }
        };
    }
}
