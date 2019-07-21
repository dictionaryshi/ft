package com.ft.br.model.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * CodeBO
 *
 * @author shichunyang
 */
@ApiModel(value = "图片验证码")
@Setter
@Getter
@ToString
public class CodeBO {
	@ApiModelProperty(value = "图片验证码唯一标识", required = true)
	private String codeId;

	@ApiModelProperty(value = "图片验证码base64字符串", required = true)
	private String img;
}
