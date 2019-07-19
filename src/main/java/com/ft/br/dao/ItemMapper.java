package com.ft.br.dao;

import com.ft.br.model.vo.ItemVO;
import com.ft.dao.stock.model.ItemDO;
import com.ft.db.constant.DbConstant;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 订单条目DAO
 *
 * @author shichunyang
 */
@Mapper
@Component
public interface ItemMapper {

	/**
	 * 增加订单条目
	 *
	 * @param itemDO 订单条目DO
	 * @return 1:增加成功
	 */
	@Insert("insert into `item` (`order_id`, `goods_id`, `goods_number`) values (#{orderId}, #{goodsId}, #{goodsNumber})")
	@SelectKey(statement = DbConstant.SELECT_LAST_INSERT_ID, keyColumn = "id", keyProperty = "id", resultType = Long.class, before = false)
	int insert(ItemDO itemDO);

	/**
	 * 根据主键修改订单项
	 *
	 * @param itemDO 订单项DO
	 * @return 1:修改成功
	 */
	@Update("update `item` t set t.`goods_number` = #{goodsNumber} where t.`id` = #{id}")
	int update(ItemDO itemDO);

	/**
	 * 删除订单项
	 *
	 * @param id 订单项主键
	 * @return 1:删除成功
	 */
	@Delete("delete from `item` where id = #{id}")
	int delete(@Param("id") int id);

	/**
	 * 根据主键查询订单项信息
	 *
	 * @param id 主键
	 * @return 订单项DO
	 */
	@Select("select * from `item` where id = #{id}")
	ItemDO selectById(@Param("id") int id);

	/**
	 * 根据订单id 查询所有订单项信息
	 *
	 * @param orderId 订单ID
	 * @return 订单项集合
	 */
	@Select("select * from `item` where order_id = #{orderId} order by `created_at` asc")
	List<ItemVO> selectByOrderId(@Param("orderId") Long orderId);
}
