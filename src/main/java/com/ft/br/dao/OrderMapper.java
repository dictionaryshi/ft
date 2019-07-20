package com.ft.br.dao;

import com.ft.br.model.dto.OrderDTO;
import com.ft.br.model.vo.OrderVO;
import com.ft.dao.stock.mapper.OrderDOMapper;
import com.ft.util.StringUtil;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;
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
		private String query(OrderDTO orderDTO) {
			StringBuilder sb = new StringBuilder();
			sb.append("where 1 = 1 ");
			if (orderDTO.getId() != null) {
				sb.append("and `id` = '").append(orderDTO.getId()).append("' ");
			}
			if (orderDTO.getStatus() != null) {
				sb.append("and `status` = ").append(orderDTO.getStatus()).append(" ");
			}
			if (!StringUtil.isNull(orderDTO.getStartDate())) {
				sb.append("and `created_at` >= '").append(orderDTO.getStartDate()).append("' ");
			}
			if (!StringUtil.isNull(orderDTO.getEndDate())) {
				sb.append("and `created_at` <= '").append(orderDTO.getEndDate()).append("' ");
			}

			return sb.toString();
		}

		public String countPagination(OrderDTO orderDTO) {
			String sql;
			sql = "select count(1) from `order` " +
					query(orderDTO);
			return sql;
		}

		public String listPagination(OrderDTO orderDTO) {
			String sql;
			sql = "select * from `order` " +
					query(orderDTO) +
					"order by `updated_at` desc " +
					"limit " + orderDTO.getStartRow() + ", " + orderDTO.getPageSize();
			return sql;
		}
	}

	/**
	 * 查询符合条件的数量
	 *
	 * @param orderDTO 条件
	 * @return 数量
	 */
	@SelectProvider(type = SqlBuilder.class, method = "countPagination")
	int countPagination(OrderDTO orderDTO);

	/**
	 * 分页查询订单
	 *
	 * @param orderDTO 条件
	 * @return 订单记录
	 */
	@SelectProvider(type = SqlBuilder.class, method = "listPagination")
	List<OrderVO> listPagination(OrderDTO orderDTO);
}
