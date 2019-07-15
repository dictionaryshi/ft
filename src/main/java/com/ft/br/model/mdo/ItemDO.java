package com.ft.br.model.mdo;

import lombok.Data;

import java.util.Date;

/**
 * 订单条目
 *
 * @author shichunyang
 */
@Data
public class ItemDO {

	private Long id;
	private Long orderId;
	private Long goodsId;
	private Integer goodsNumber;
	private Date createdAt;
	private Date updatedAt;
}
