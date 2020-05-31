package com.ft.br.model.ao.sso;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

/**
 * LoginAO
 *
 * @author shichunyang
 */
@ApiModel("登录录入数据")
@Getter
@Setter
@ToString
public class LoginAO {
    @NotBlank(message = "用户名不能为空")
    @ApiModelProperty(value = "用户名", required = true)
    private String username;

    @NotBlank(message = "密码不能为空")
    @ApiModelProperty(value = "密码", required = true)
    private String password;

    @NotBlank(message = "验证码不能为空")
    @ApiModelProperty(value = "验证码", required = true)
    private String code;

    @NotBlank(message = "验证码唯一标识不能为空")
    @ApiModelProperty(value = "验证码唯一标识", required = true)
    private String codeId;
}
