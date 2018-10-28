package com.ft.dao;

import com.ft.util.StringUtil;
import com.ft.model.mdo.StockLogDO;
import com.ft.model.dto.StockLogDTO;
import com.ft.model.vo.StockLogVO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * StockLogMapper
 *
 * @author shichunyang
 */
@Mapper
@Component
public interface StockLogMapper {

	class SqlBuilder {

		private String query(StockLogDTO stockLogDTO) {
			StringBuilder sb = new StringBuilder();
			sb.append("where 1 = 1 ");
			if (stockLogDTO.getType() != null) {
				sb.append("and `type` = ").append(stockLogDTO.getType()).append(" ");
			}
			if (stockLogDTO.getGoodsId() != null) {
				sb.append("and `goods_id` = ").append(stockLogDTO.getGoodsId()).append(" ");
			}
			if (!StringUtil.isNull(stockLogDTO.getOrderId())) {
				sb.append("and `order_id` = '").append(stockLogDTO.getOrderId()).append("' ");
			}
			if (!StringUtil.isNull(stockLogDTO.getStartDate())) {
				sb.append("and `updated_at` >= '").append(stockLogDTO.getStartDate()).append("' ");
			}
			if (!StringUtil.isNull(stockLogDTO.getEndDate())) {
				sb.append("and `updated_at` <= '").append(stockLogDTO.getEndDate()).append("' ");
			}

			return sb.toString();
		}

		public String countPagination(StockLogDTO stockLogDTO) {
			String sql;
			sql = "select count(1) from `stock_log` " +
					query(stockLogDTO);
			return sql;
		}

		public String listPagination(StockLogDTO stockLogDTO) {
			String sb;
			sb = "select * from `stock_log` " +
					query(stockLogDTO) +
					"order by `id` desc " +
					"limit " + stockLogDTO.getStartRow() + ", " + stockLogDTO.getPageSize();
			return sb;
		}
	}

	/**
	 * 添加出/入库记录
	 *
	 * @param stockLogDO 出入库日志对象
	 * @return 1:添加成功
	 */
	@Insert({
			"insert into `stock_log` ",
			"(`operator`, `type`, `type_detail`, `goods_id`, `goods_number`, `order_id`, `remark`) values ",
			"(#{operator}, #{type}, #{typeDetail}, #{goodsId}, #{goodsNumber}, #{orderId}, #{remark})"
	})
	int insert(StockLogDO stockLogDO);

	/**
	 * 查询符合条件的数量
	 *
	 * @param stockLogDTO 条件DTO
	 * @return 数量
	 */
	@SelectProvider(type = SqlBuilder.class, method = "countPagination")
	int countPagination(StockLogDTO stockLogDTO);

	/**
	 * 分页查询出入库记录
	 *
	 * @param stockLogDTO 条件
	 * @return 本次查询记录
	 */
	@SelectProvider(type = SqlBuilder.class, method = "listPagination")
	List<StockLogVO> listPagination(StockLogDTO stockLogDTO);
}
