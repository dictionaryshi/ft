package com.ft.br.dao;

import com.ft.br.model.ao.OrderListAO;
import com.ft.dao.stock.mapper.OrderDOMapper;
import com.ft.dao.stock.model.OrderDO;
import com.ft.db.util.MybatisUtil;
import com.ft.util.StringUtil;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.jdbc.SQL;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * OrderMapper
 *
 * @author shichunyang
 */
@Component
@Mapper
public interface OrderMapper extends OrderDOMapper {

	class SqlBuilder {
		private void pageWhere(SQL sql, OrderListAO ao) {
			if (ao.getId() != null) {
				sql.WHERE("id = #{id}");
			}

			if (ao.getStatus() != null) {
				sql.WHERE("status = #{status}");
			}

			if (!StringUtil.isNull(ao.getStartTime())) {
				sql.WHERE("created_at >= #{startTime}");
			}

			if (!StringUtil.isNull(ao.getEndTime())) {
				sql.WHERE("created_at <= #{endTime}");
			}
		}

		public String countPagination(OrderListAO ao) {
			SQL sql = new SQL();

			sql.SELECT("count(1)");

			sql.FROM("`order`");

			this.pageWhere(sql, ao);

			return sql.toString();
		}

		public String listPagination(OrderListAO ao) {
			SQL sql = new SQL();

			sql.SELECT("*");

			sql.FROM("`order`");

			this.pageWhere(sql, ao);

			sql.ORDER_BY("id desc");

			return MybatisUtil.limit(sql, ao);
		}
	}

	/**
	 * 查询符合条件的数量
	 *
	 * @param orderListAO 条件
	 * @return 数量
	 */
	@SelectProvider(type = SqlBuilder.class, method = "countPagination")
	int countPagination(OrderListAO orderListAO);

	/**
	 * 分页查询订单
	 *
	 * @param orderListAO 条件
	 * @return 订单记录
	 */
	@SelectProvider(type = SqlBuilder.class, method = "listPagination")
	List<OrderDO> listPagination(OrderListAO orderListAO);
}
