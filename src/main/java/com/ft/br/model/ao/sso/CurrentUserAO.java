package com.ft.br.model.ao.sso;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

/**
 * CurrentUserAO
 *
 * @author shichunyang
 */
@ApiModel("获取当前登陆用户信息")
@Setter
@Getter
@ToString
public class CurrentUserAO {
    @NotBlank(message = "sso token不为空")
    @ApiModelProperty(value = "sso token", required = true)
    private String token;
}
