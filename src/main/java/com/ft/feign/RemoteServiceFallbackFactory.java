package com.ft.feign;

import com.ft.model.mdo.UserDO;
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
			public String user(UserDO userDO) {
				return "feign user down";
			}

			@Override
			public String get(String username, int age) {
				return "feign get down";
			}
		};
	}
}
