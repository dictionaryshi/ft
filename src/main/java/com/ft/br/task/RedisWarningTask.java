package com.ft.br.task;

import com.ft.redis.base.HashOperationsCache;
import com.ft.redis.plugin.RedisWarning;
import com.ft.util.StringUtil;
import com.ft.web.plugin.MailUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 流量报警
 *
 * @author shichunyang
 */
@Component
public class RedisWarningTask {
	@Autowired
	private HashOperationsCache<String, String, String> hashOperationsCache;

	@Autowired
	private MailUtil mailUtil;

	@Scheduled(cron = "0/30 * * * * ?")
	public void warn() {
		Map<String, String> entries = hashOperationsCache.entries(RedisWarning.REDIS_WARNING_MAP);
		hashOperationsCache.delete(RedisWarning.REDIS_WARNING_MAP);
		if (StringUtil.isEmpty(entries)) {
			return;
		}
		entries.values().forEach(message -> {
			try {
				mailUtil.send(new String[]{"903031015@qq.com"}, "903031015@qq.com", "流量警报", message, null, null);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}
}
