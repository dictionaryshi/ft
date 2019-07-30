package com.ft.br.task;

import com.ft.redis.base.SetOperationsCache;
import com.ft.redis.base.ZSetOperationsCache;
import com.ft.redis.plugin.RedisWarning;
import com.ft.util.BigDecimalUtil;
import com.ft.util.DateUtil;
import com.ft.util.LogUtil;
import com.ft.util.ObjectUtil;
import com.ft.util.model.LogAO;
import com.ft.util.model.LogBO;
import com.ft.web.plugin.MailUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 流量报警
 *
 * @author shichunyang
 */
@Slf4j
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

		long startIndex = 0;

		long endIndex = -1;

		for (String warnKey : warnKeys) {
			Set<ZSetOperations.TypedTuple<String>> flowWarningSet = zSetOperationsCache.rangeWithScores(warnKey, startIndex, endIndex);
			if (ObjectUtil.isEmpty(flowWarningSet) || flowWarningSet.size() < 10) {
				continue;
			}

			List<ZSetOperations.TypedTuple<String>> flowWarningList = new ArrayList<>(flowWarningSet);

			flowWarningList.sort(Comparator.comparing(o -> Integer.valueOf(o.getValue())));

			ZSetOperations.TypedTuple<String> startTypedTuple = flowWarningList.get(0);
			ZSetOperations.TypedTuple<String> endTypedTuple = flowWarningList.get(9);
			int startTime = Integer.parseInt(Objects.requireNonNull(startTypedTuple.getValue()));
			int endTime = Integer.parseInt(Objects.requireNonNull(endTypedTuple.getValue()));
			if (endTime - startTime != 9) {
				zSetOperationsCache.remove(warnKey, startTypedTuple.getValue());
				continue;
			}

			double front = 0;
			for (int i = 0; i < 5; i++) {
				ZSetOperations.TypedTuple<String> typedTuple = flowWarningList.get(i);
				front += typedTuple.getScore();
			}

			double after = 0;
			for (int i = 5; i < 10; i++) {
				ZSetOperations.TypedTuple<String> typedTuple = flowWarningList.get(i);
				after += typedTuple.getScore();
			}

			double flow = front + after;

			double flowIncrement = after - front;
			double flowIncrementRatio = BigDecimalUtil.divide(flowIncrement + "", front + "", 2).doubleValue();
			double targetRatio = 2.0;
			int minimumFlow = 10;
			if (flowIncrementRatio > targetRatio && flow > minimumFlow) {
				LogBO logBO = LogUtil.log("流量报警",
						LogAO.build("applicationName", warnKey),
						LogAO.build("flow", flow + ""),
						LogAO.build("front", front + ""),
						LogAO.build("after", after + ""),
						LogAO.build("flowIncrementRatio", flowIncrementRatio + ""));
				log.info(logBO.getLogPattern(), logBO.getParams());
				String message = "服务==>" + warnKey
						+ ", 时间==>" + DateUtil.date2Str(DateUtil.getCurrentDate(), DateUtil.DATE_PATTERN_MILLISECOND)
						+ ", 5秒内流量陡增超过200%"
						+ ", flow==>" + flow
						+ ", front==>" + front
						+ ", after==>" + after
						+ ", flowIncrementRatio==>" + flowIncrementRatio;

				try {
					mailUtil.send(new String[]{"903031015@qq.com"}, "903031015@qq.com", "流量警报", message, null, null);
				} catch (Exception e) {
					logBO = LogUtil.log("流量警报error", e);
					log.error(logBO.getLogPattern(), logBO.getParams());
				}
			}

			zSetOperationsCache.remove(warnKey, startTypedTuple.getValue());
		}
	}
}
