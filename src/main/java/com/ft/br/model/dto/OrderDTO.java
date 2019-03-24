package com.ft.br.model.dto;

import com.ft.br.model.mdo.OrderDO;
import com.ft.model.mdo.OrderDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * OrderDTO
 *
 * @author shichunyang
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class OrderDTO extends OrderDO {

	/**
	 * 开始日期
	 */
	private String startDate;
	/**
	 * 结束日期
	 */
	private String endDate;

	private Integer startRow;
	private Integer pageSize;
}
