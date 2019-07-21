package com.ft.br.model.bo;

import com.ft.web.model.UserBO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * TokenBO
 *
 * @author shichunyang
 */
@ApiModel("sso token 相关数据")
@Getter
@Setter
@ToString
public class TokenBO {
	@ApiModelProperty(value = "sso token", required = true)
	private String token;

	@ApiModelProperty(value = "登陆用户信息", required = true)
	private UserBO user;
}
