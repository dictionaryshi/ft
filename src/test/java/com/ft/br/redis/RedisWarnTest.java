package com.ft.br.redis;

import com.ft.web.util.HttpUtil;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class RedisWarnTest {
	@Test
	public void warnTest() throws Exception {
		String url = "http://localhost:9001";
		Map<String, String> map = new HashMap<>(16);
		map.put("username", "scy");
		for (int i = 0; i < 10000; i++) {
			if (i < 50) {
				HttpUtil.get(url, map, 3_000, 3_000);
				Thread.sleep(100L);
			} else if (i < 550) {
				HttpUtil.get(url, map, 3_000, 3_000);
				Thread.sleep(10L);
			} else if (i < 5550) {
				HttpUtil.get(url, map, 3_000, 3_000);
				Thread.sleep(1L);
			}
		}
	}
}
