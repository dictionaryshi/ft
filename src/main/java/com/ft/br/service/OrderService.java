package com.ft.br.service;

import com.ft.br.model.ao.order.OrderAddUpdateAO;
import com.ft.br.model.ao.order.OrderGetAO;
import com.ft.br.model.bo.OrderBO;

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
}
