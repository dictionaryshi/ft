package com.ft.br.dao;

import com.ft.br.model.vo.ItemVO;
import com.ft.dao.stock.mapper.ItemDOMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 订单条目DAO
 *
 * @author shichunyang
 */
@Mapper
@Component
public interface ItemMapper extends ItemDOMapper {

	/**
	 * 根据订单id 查询所有订单项信息
	 *
	 * @param orderId 订单ID
	 * @return 订单项集合
	 */
	@Select("select * from `item` where order_id = #{orderId} order by `id` asc")
	List<ItemVO> selectByOrderId(Long orderId);
}
