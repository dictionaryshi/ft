package com.ft.br.task;

import com.ft.redis.base.SetOperationsCache;
import com.ft.redis.base.ZSetOperationsCache;
import com.ft.redis.plugin.RedisWarning;
import com.ft.util.ObjectUtil;
import com.ft.util.StringUtil;
import com.ft.web.plugin.MailUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * 流量报警
 *
 * @author shichunyang
 */
@Component
public class RedisWarningTask {
	@Autowired
	private ZSetOperationsCache<String, String> zSetOperationsCache;

	@Autowired
	private SetOperationsCache<String, String> setOperationsCache;

	@Autowired
	private MailUtil mailUtil;

	@Scheduled(cron = "0/1 * * * * ?")
	public void warn() {
		Set<String> warnKeys = setOperationsCache.members(RedisWarning.REDIS_WARNING_SETS);
		if (ObjectUtil.isEmpty(warnKeys)) {
			return;
		}
		long startStore = 0;
		long endStore = System.currentTimeMillis();

		for (String warnKey : warnKeys) {
			Set<ZSetOperations.TypedTuple<String>> sets = zSetOperationsCache.reverseRangeByScoreWithScores(warnKey, startStore, endStore, 0, -1);
			if (ObjectUtil.isEmpty(sets)) {
				continue;
			}

			zSetOperationsCache.removeRangeByScore(warnKey, startStore, endStore);

			ZSetOperations.TypedTuple<String> messageTypedTuple = sets.stream().findFirst().orElse(null);
			if (messageTypedTuple == null) {
				continue;
			}
			String message = messageTypedTuple.getValue();
			try {
				mailUtil.send(new String[]{"903031015@qq.com"}, "903031015@qq.com", "流量警报", message, null, null);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
