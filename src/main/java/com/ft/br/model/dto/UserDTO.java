package com.ft.br.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ft.util.DateUtil;
import com.ft.web.constant.SwaggerConstant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * UserPO
 *
 * @author shichunyang
 */
@ApiModel(value = "用户对象", description = "存储用户相关信息")
@Data
public class UserDTO {

	@ApiModelProperty(value = "用户id", required = true, dataType = SwaggerConstant.DATA_TYPE_INT, example = "0")
	private Integer id;

	@ApiModelProperty(value = "姓名", required = true, dataType = SwaggerConstant.DATA_TYPE_STRING, example = "春阳")
	private String name;

	@ApiModelProperty(value = "年龄", dataType = SwaggerConstant.DATA_TYPE_INT, example = "0")
	private Integer age;

	@ApiModelProperty(value = "创建时间", required = true, dataType = SwaggerConstant.DATA_TYPE_STRING, example = DateUtil.DEFAULT_DATE_FORMAT)
	@JsonProperty("crt_time")
	private String crtTime;
}
