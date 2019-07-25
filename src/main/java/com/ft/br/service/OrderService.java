package com.ft.br.service;

import com.ft.br.model.ao.order.OrderAddAO;

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
	boolean createOrder(OrderAddAO orderAddAO);
}
