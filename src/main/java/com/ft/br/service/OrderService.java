package com.ft.br.service;

import com.ft.br.model.ao.item.ItemAddAO;
import com.ft.br.model.ao.item.ItemUpdateAO;
import com.ft.br.model.ao.order.OrderAddUpdateAO;
import com.ft.br.model.ao.order.OrderGetAO;
import com.ft.br.model.ao.order.OrderListAO;
import com.ft.br.model.bo.ItemBO;
import com.ft.br.model.bo.OrderBO;
import com.ft.db.model.PageResult;

import java.util.List;

/**
 * OrderService
 *
 * @author shichunyang
 */
public interface OrderService {
	/**
	 * 创建订单
	 *
	 * @param orderAddAO 订单信息
	 * @return true 创建成功
	 */
	boolean createOrder(OrderAddUpdateAO orderAddAO);

	/**
	 * 修改订单
	 *
	 * @param orderAddUpdateAO 订单信息
	 * @return true 修改成功
	 */
	boolean updateOrder(OrderAddUpdateAO orderAddUpdateAO);

	/**
	 * 根据主键查询订单信息
	 *
	 * @param orderGetAO 订单主键
	 * @return 订单信息
	 */
	OrderBO getOrderById(OrderGetAO orderGetAO);

	/**
	 * 分页查询订单信息
	 *
	 * @param orderListAO 分页条件
	 * @return 订单信息集合
	 */
	PageResult<OrderBO> listByPage(OrderListAO orderListAO);

	/**
	 * 添加订单项
	 *
	 * @param itemAO 订单项
	 * @return true 添加成功
	 */
	boolean addItem(ItemAddAO itemAO);

	/**
	 * 查询订单下的所有订单项信息
	 *
	 * @param orderId 订单id
	 * @return 所有订单项信息
	 */
	List<ItemBO> listItems(long orderId);

	/**
	 * 删除订单项
	 *
	 * @param itemId 订单项id
	 * @return true 删除成功
	 */
	boolean deleteItem(int itemId);

	/**
	 * 修改订单项
	 *
	 * @param itemUpdateAO 订单项信息
	 * @return true 修改成功
	 */
	boolean updateItem(ItemUpdateAO itemUpdateAO);

	/**
	 * 确认订单成功
	 *
	 * @param orderId 订单id
	 * @param userId  操作人
	 * @return true 确认成功
	 */
	boolean orderSuccess(long orderId, int userId);

	/**
	 * 确认订单
	 *
	 * @param orderId 订单id
	 * @param userId  用户id
	 * @return true 确认成功
	 */
	boolean confirmOrder(Long orderId, int userId);
}
