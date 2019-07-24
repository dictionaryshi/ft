package com.ft.br.model.bo;

import com.ft.util.DateUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * StockLogBO
 *
 * @author shichunyang
 */
@ApiModel("仓库操作信息")
@Getter
@Setter
@ToString
public class StockLogBO {

	@ApiModelProperty(value = "仓库操作记录id", required = true, example = "10")
	private Integer id;

	@ApiModelProperty(value = "操作人", required = true, example = "10")
	private Integer operator;

	@ApiModelProperty(value = "操作类型", required = true, example = "1")
	private Integer type;

	@ApiModelProperty(value = "详细操作类型", required = true, example = "10")
	private Integer typeDetail;

	@ApiModelProperty(value = "商品id", required = true, example = "10")
	private Integer goodsId;

	@ApiModelProperty(value = "操作前库存", required = true, example = "10")
	private Integer beforeStockNumber;

	@ApiModelProperty(value = "商品数量", required = true, example = "10")
	private Integer goodsNumber;

	@ApiModelProperty(value = "操作后库存", required = true, example = "10")
	private Integer afterStockNumber;

	@ApiModelProperty(value = "订单id", required = true, example = "10")
	private Long orderId;

	@ApiModelProperty(value = "操作备注", required = true)
	private String remark;

	@ApiModelProperty(value = "操作时间", required = true, example = DateUtil.TIMESTAMP_DEFAULT_TIME)
	private Date createdAt;


	@ApiModelProperty(value = "操作人", required = true)
	private String operatorName;

	@ApiModelProperty(value = "操作类型", required = true)
	private String typeCH;

	@ApiModelProperty(value = "详细操作类型", required = true)
	private String typeDetailCH;

	@ApiModelProperty(value = "商品名称", required = true)
	private String goodsName;
}
