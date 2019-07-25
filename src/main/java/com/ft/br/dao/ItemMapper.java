package com.ft.br.dao;

import com.ft.dao.stock.mapper.ItemDOMapper;
import com.ft.dao.stock.model.ItemDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
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
	List<ItemDO> selectByOrderId(Long orderId);

	@Select("select * from `item` where order_id = #{orderId} and goods_id = #{goodsId} limit 1")
	ItemDO selectByOrderIdAndGoodsId(@Param("orderId") Long orderId, @Param("goodsId") int goodsId);
}
