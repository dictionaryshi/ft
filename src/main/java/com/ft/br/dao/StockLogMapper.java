package com.ft.br.dao;

import com.ft.br.model.ao.stock.StockLogListAO;
import com.ft.dao.stock.mapper.StockLogDOMapper;
import com.ft.dao.stock.model.StockLogDO;
import com.ft.db.util.MybatisUtil;
import com.ft.util.StringUtil;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.jdbc.SQL;
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
		private void pageWhere(SQL sql, StockLogListAO ao) {
			if (ao.getType() != null) {
				sql.WHERE("type = #{type}");
			}
			if (ao.getGoodsId() != null) {
				sql.WHERE("goods_id = #{goodsId}");
			}
			if (ao.getOrderId() != null) {
				sql.WHERE("order_id = #{orderId}");
			}
			if (!StringUtil.isNull(ao.getStartTime())) {
				sql.WHERE("created_at >= #{startTime}");
			}
			if (!StringUtil.isNull(ao.getEndTime())) {
				sql.WHERE("created_at <= #{endTime}");
			}
		}

		public String countPagination(StockLogListAO ao) {
			SQL sql = new SQL();

			sql.SELECT("count(1)");

			sql.FROM("stock_log");

			this.pageWhere(sql, ao);

			return sql.toString();
		}

		public String listPagination(StockLogListAO ao) {
			SQL sql = new SQL();

			sql.SELECT("*");

			sql.FROM("stock_log");

			this.pageWhere(sql, ao);

			sql.ORDER_BY("id desc");

			return MybatisUtil.limit(sql, ao);
		}
	}

	/**
	 * 查询符合条件的数量
	 *
	 * @param stockLogListAO 条件DTO
	 * @return 数量
	 */
	@SelectProvider(type = SqlBuilder.class, method = "countPagination")
	int countPagination(StockLogListAO stockLogListAO);

	/**
	 * 分页查询出入库记录
	 *
	 * @param stockLogListAO 条件
	 * @return 本次查询记录
	 */
	@SelectProvider(type = SqlBuilder.class, method = "listPagination")
	List<StockLogDO> listPagination(StockLogListAO stockLogListAO);
}
