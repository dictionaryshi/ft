package com.ft.br.service;

import com.ft.br.constant.StockLogTypeDetailEnum;
import com.ft.br.constant.StockLogTypeEnum;
import com.ft.br.dao.GoodsMapper;
import com.ft.br.dao.OrderMapper;
import com.ft.br.dao.StockLogMapper;
import com.ft.br.dao.UserMapper;
import com.ft.br.model.dto.StockLogDTO;
import com.ft.br.model.mdo.GoodsDO;
import com.ft.br.model.mdo.StockLogDO;
import com.ft.br.model.vo.OrderVO;
import com.ft.br.model.vo.StockLogVO;
import com.ft.db.constant.DbConstant;
import com.ft.db.model.PageParam;
import com.ft.db.model.PageResult;
import com.ft.redis.base.ValueOperationsCache;
import com.ft.redis.lock.RedisLock;
import com.ft.util.StringUtil;
import com.ft.util.exception.FtException;
import com.ft.web.model.UserDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 出入库业务
 *
 * @author shichunyang
 */
@Service
public class StockLogService {

	@Autowired
	private StockLogMapper stockLogMapper;

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private GoodsMapper goodsMapper;

	@Autowired
	private OrderMapper orderMapper;

	@Autowired
	private ValueOperationsCache<String, String> valueOperationsCache;

	/**
	 * 分页查询出入库记录
	 *
	 * @param stockLogDTO 分页查询条件
	 * @return 本次查询记录
	 */
	public PageResult<StockLogVO> list(StockLogDTO stockLogDTO, PageParam pageParam) {
		PageResult<StockLogVO> pageResult = new PageResult<>();
		pageResult.setPage(pageParam.getPage());
		pageResult.setLimit(pageParam.getLimit());


		int count = stockLogMapper.countPagination(stockLogDTO);
		if (count == 0) {
			pageResult.setTotal(0);
			pageResult.setList(new ArrayList<>());
			return pageResult;
		}

		stockLogDTO.setStartRow(pageParam.getStartRowNumber());
		stockLogDTO.setPageSize(pageParam.getLimit());

		List<StockLogVO> stockLogs = stockLogMapper.listPagination(stockLogDTO);
		this.format(stockLogs);

		pageResult.setTotal(count);
		pageResult.setList(stockLogs);

		return pageResult;
	}

	private void format(List<StockLogVO> stockLogs) {
		if (stockLogs.isEmpty()) {
			return;
		}
		stockLogs.forEach(stockLog -> {
			long operator = stockLog.getOperator();
			UserDO user = userMapper.getUserById(operator);
			if (user != null) {
				stockLog.setOperatorName(user.getUsername());
			}

			String typeCh = null;
			StockLogTypeEnum stockLogTypeEnum = StockLogTypeEnum.getByType(stockLog.getType());
			if (stockLogTypeEnum != null) {
				typeCh = stockLogTypeEnum.getMessage();
			}
			stockLog.setTypeCH(typeCh);

			long goodsId = stockLog.getGoodsId();
			GoodsDO goodsDO = goodsMapper.getGoodsById(goodsId);
			if (goodsDO != null) {
				stockLog.setGoodsName(goodsDO.getName());
			}
		});
	}

	/**
	 * 出入库操作
	 *
	 * @param stockLogDO 仓库对象
	 * @return true:操作成功
	 */
	@Transactional(value = DbConstant.DB_CONSIGN + DbConstant.TRAN_SACTION_MANAGER, propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class)
	public boolean storage(StockLogDO stockLogDO) {
		// 出入库类型
		Integer type = stockLogDO.getType();
		// 商品id
		long goodsId = stockLogDO.getGoodsId();
		// 出入库商品数量
		int goodsNumber = stockLogDO.getGoodsNumber();

		if (goodsNumber < 1) {
			FtException.throwException("仓库操作失败, 商品数量不能小于1");
		}

		GoodsDO goodsDO = goodsMapper.getGoodsById(goodsId);
		if (goodsDO == null) {
			FtException.throwException("商品不存在，操作仓库失败");
		}

		String defaultOrderId = "0";
		if (!stockLogDO.getOrderId().equals(defaultOrderId)) {
			OrderVO order = orderMapper.getOrderById(stockLogDO.getOrderId());
			if (order == null) {
				FtException.throwException("仓库操作失败, 订单不存在");
			}
		}

		if (type == StockLogTypeEnum.IN.getType().shortValue()) {
			// 入库操作
			goodsMapper.updateNumber(goodsId, goodsNumber);
			stockLogDO.setTypeDetail(StockLogTypeDetailEnum.IN_PERSON.getTypeDetail());
			stockLogMapper.insert(stockLogDO);
		} else if (type == StockLogTypeEnum.OUT.getType().shortValue()) {
			// 出库操作
			if (goodsNumber > goodsDO.getNumber()) {
				FtException.throwException("出库失败, 商品库存数量不足");
			}

			String lockKey = StringUtil.append(StringUtil.REDIS_SPLIT, "storage", "goods", goodsId + "");
			RedisLock redisLock = new RedisLock(valueOperationsCache);
			redisLock.lock(lockKey, 10_000L);
			goodsDO = goodsMapper.getGoodsById(goodsId);
			if (goodsNumber <= goodsDO.getNumber()) {
				goodsMapper.updateNumber(goodsId, goodsNumber * -1);
				stockLogDO.setTypeDetail(StockLogTypeDetailEnum.OUT_PERSON.getTypeDetail());
				stockLogMapper.insert(stockLogDO);

				redisLock.unlock(lockKey);
			} else {
				redisLock.unlock(lockKey);
				return false;
			}
		} else {
			FtException.throwException("未知操作仓库类型");
		}
		return true;
	}
}
