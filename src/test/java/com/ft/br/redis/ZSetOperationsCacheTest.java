package com.ft.br.redis;

import com.ft.redis.base.ZSetOperationsCache;
import com.ft.util.JsonUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Set;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ZSetOperationsCacheTest {
	@Autowired
	private ZSetOperationsCache<String, String> zSetOperationsCache;

	private final String zset = "zet_test";

	@Test
	public void add() {
		Boolean xt = zSetOperationsCache.add(zset, "xt", 10);
		Boolean cy = zSetOperationsCache.add(zset, "cy", 30);
		Boolean zgl = zSetOperationsCache.add(zset, "zgl", 20);
		Boolean smy = zSetOperationsCache.add(zset, "smy", 40);
		System.out.println();
	}

	@Test
	public void size() {
		Long size = zSetOperationsCache.size(zset);
		System.out.println(size);
	}

	@Test
	public void count() {
		Long count = zSetOperationsCache.count(zset, 10, 30);
		System.out.println(count);
	}

	@Test
	public void incrementScore() {
		Double smy = zSetOperationsCache.incrementScore(zset, "smy", 60);
		System.out.println(smy);
	}

	@Test
	public void rangeWithScores() {
		Set<ZSetOperations.TypedTuple<String>> set1s = zSetOperationsCache.rangeWithScores(zset, 0, 0);
		Set<ZSetOperations.TypedTuple<String>> set2s = zSetOperationsCache.rangeWithScores(zset, 1, 1);
		Set<ZSetOperations.TypedTuple<String>> set3s = zSetOperationsCache.rangeWithScores(zset, 2, 2);
		Set<ZSetOperations.TypedTuple<String>> set4s = zSetOperationsCache.rangeWithScores(zset, 3, 3);
		System.out.println(JsonUtil.object2Json(set1s));
		System.out.println(JsonUtil.object2Json(set2s));
		System.out.println(JsonUtil.object2Json(set3s));
		System.out.println(JsonUtil.object2Json(set4s));
	}

	@Test
	public void rangeByScoreWithScores() {
		Set<ZSetOperations.TypedTuple<String>> set = zSetOperationsCache.rangeByScoreWithScores(zset, 10, 40, 0, -1);
		System.out.println(JsonUtil.object2Json(set));
	}

	@Test
	public void rank() {
		Long rank1 = zSetOperationsCache.rank(zset, "xt");
		Long rank2 = zSetOperationsCache.rank(zset, "zgl");
		Long rank3 = zSetOperationsCache.rank(zset, "cy");
		Long rank4 = zSetOperationsCache.rank(zset, "smy");
		System.out.println(rank1);
		System.out.println(rank2);
		System.out.println(rank3);
		System.out.println(rank4);
	}

	@Test
	public void remove() {
		Long removeCount = zSetOperationsCache.remove(zset, "xt", "zgl", "cy", "smy");
		System.out.println(removeCount);
	}

	@Test
	public void removeRange() {
		Long removeCount = zSetOperationsCache.removeRange(zset, 1, 2);
		System.out.println(removeCount);
	}

	@Test
	public void removeRangeByScore() {
		Long removeCount = zSetOperationsCache.removeRangeByScore(zset, 10, 40);
		System.out.println(removeCount);
	}

	@Test
	public void reverseRangeWithScores() {
		Set<ZSetOperations.TypedTuple<String>> set1 = zSetOperationsCache.reverseRangeWithScores(zset, 0, 0);
		Set<ZSetOperations.TypedTuple<String>> set2 = zSetOperationsCache.reverseRangeWithScores(zset, 1, 1);
		Set<ZSetOperations.TypedTuple<String>> set3 = zSetOperationsCache.reverseRangeWithScores(zset, 2, 2);
		Set<ZSetOperations.TypedTuple<String>> set4 = zSetOperationsCache.reverseRangeWithScores(zset, 3, 3);
		System.out.println(JsonUtil.object2Json(set1));
		System.out.println(JsonUtil.object2Json(set2));
		System.out.println(JsonUtil.object2Json(set3));
		System.out.println(JsonUtil.object2Json(set4));
	}

	@Test
	public void reverseRangeByScoreWithScores() {
		Set<ZSetOperations.TypedTuple<String>> sets = zSetOperationsCache.reverseRangeByScoreWithScores(zset, 10, 40, 0, -1);
		System.out.println(JsonUtil.object2Json(sets));
	}

	@Test
	public void reverseRank() {
		Long rank1 = zSetOperationsCache.reverseRank(zset, "xt");
		Long rank2 = zSetOperationsCache.reverseRank(zset, "zgl");
		Long rank3 = zSetOperationsCache.reverseRank(zset, "cy");
		Long rank4 = zSetOperationsCache.reverseRank(zset, "smy");
		System.out.println(rank1);
		System.out.println(rank2);
		System.out.println(rank3);
		System.out.println(rank4);
	}

	@Test
	public void score() {
		Double smy = zSetOperationsCache.score(zset, "smy");
		System.out.println(smy);
	}
}
