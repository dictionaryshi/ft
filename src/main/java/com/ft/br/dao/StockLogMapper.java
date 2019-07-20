package com.ft.br.dao;

import com.ft.br.model.dto.StockLogDTO;
import com.ft.br.model.vo.StockLogVO;
import com.ft.dao.stock.mapper.StockLogDOMapper;
import com.ft.util.StringUtil;
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
public interface StockLogMapper extends StockLogDOMapper {

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
			if (stockLogDTO.getOrderId() != null) {
				sb.append("and `order_id` = '").append(stockLogDTO.getOrderId()).append("' ");
			}
			if (!StringUtil.isNull(stockLogDTO.getStartDate())) {
				sb.append("and `created_at` >= '").append(stockLogDTO.getStartDate()).append("' ");
			}
			if (!StringUtil.isNull(stockLogDTO.getEndDate())) {
				sb.append("and `created_at` <= '").append(stockLogDTO.getEndDate()).append("' ");
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
