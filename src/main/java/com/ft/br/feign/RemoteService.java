package com.ft.br.feign;

import com.ft.rpc.api.service.FeignService;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * 远程service
 *
 * @author shichunyang
 */
@FeignClient(name = "feign", fallbackFactory = RemoteServiceFallbackFactory.class)
public interface RemoteService extends FeignService {
}
