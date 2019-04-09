package com.ft.br.model.mdo;

import lombok.Data;

import java.util.Date;

/**
 * 订单模型
 *
 * @author shichunyang
 */
@Data
public class OrderDO {

	/**
	 * 订单号
	 */
	private String id;
	/**
	 * 操作人
	 */
	private Long operator;
	/**
	 * 订单状态{@link OrderTypeEnum}
	 */
	private Short status;
	/**
	 * 客户姓名
	 */
	private String username;
	/**
	 * 客户电话
	 */
	private String phone;
	/**
	 * 送货地址
	 */
	private String address;
	/**
	 * 订单总金额
	 */
	private Double totalAmount;
	/**
	 * 订单备注
	 */
	private String remark;
	/**
	 * 创建时间
	 */
	private Date createdAt;
	/**
	 * 修改时间
	 */
	private Date updatedAt;
}
