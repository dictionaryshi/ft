package com.ft.br.dao;

import com.ft.br.model.mdo.OrderDO;
import com.ft.util.StringUtil;
import com.ft.model.mdo.OrderDO;
import com.ft.model.dto.OrderDTO;
import com.ft.model.vo.OrderVO;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * OrderMapper
 *
 * @author shichunyang
 */
@Component
@Mapper
public interface OrderMapper {

	class SqlBuilder {
		public String update(OrderDO orderDO) {
			StringBuilder sb = new StringBuilder("update `order` ");
			sb.append("set `operator` = ").append(orderDO.getOperator());
			if (orderDO.getStatus() != null) {
				sb.append(", `status` = ").append(orderDO.getStatus());
			}
			if (!StringUtil.isNull(orderDO.getUsername())) {
				sb.append(", `username` = '").append(orderDO.getUsername()).append("'");
			}
			if (!StringUtil.isNull(orderDO.getPhone())) {
				sb.append(", `phone` = '").append(orderDO.getPhone()).append("'");
			}
			if (!StringUtil.isNull(orderDO.getAddress())) {
				sb.append(", `address` = '").append(orderDO.getAddress()).append("'");
			}
			if (orderDO.getTotalAmount() != null) {
				sb.append(", `total_amount` = ").append(orderDO.getTotalAmount());
			}
			if (!StringUtil.isNull(orderDO.getRemark())) {
				sb.append(", `remark` = '").append(orderDO.getRemark()).append("'");
			}
			sb.append(" where `id` = '").append(orderDO.getId()).append("'");
			return sb.toString();
		}

		private String query(OrderDTO orderDTO) {
			StringBuilder sb = new StringBuilder();
			sb.append("where 1 = 1 ");
			if (!StringUtil.isNull(orderDTO.getId())) {
				sb.append("and `id` = '").append(orderDTO.getId()).append("' ");
			}
			if (orderDTO.getStatus() != null) {
				sb.append("and `status` = ").append(orderDTO.getStatus()).append(" ");
			}
			if (!StringUtil.isNull(orderDTO.getStartDate())) {
				sb.append("and `updated_at` >= '").append(orderDTO.getStartDate()).append("' ");
			}
			if (!StringUtil.isNull(orderDTO.getEndDate())) {
				sb.append("and `updated_at` <= '").append(orderDTO.getEndDate()).append("' ");
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
	 * 添加订单信息
	 *
	 * @param orderDO 订单信息
	 * @return 1:添加成功
	 */
	@Insert("insert into `order` (`id`, `operator`, `status`, `username`, `phone`, `address`, `total_amount`, `remark`) " +
			"values (#{id}, #{operator}, #{status}, #{username}, #{phone}, #{address}, #{totalAmount}, #{remark})")
	int insert(OrderDO orderDO);

	/**
	 * 修改订单信息
	 *
	 * @param orderDO 订单信息
	 * @return 1:修改成功
	 */
	@UpdateProvider(type = SqlBuilder.class, method = "update")
	int update(OrderDO orderDO);

	/**
	 * 根据主键查询订单信息
	 *
	 * @param id 主键
	 * @return 订单信息
	 */
	@Select("select * from `order` where `id` = #{id}")
	OrderVO getOrderById(@Param("id") String id);

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
