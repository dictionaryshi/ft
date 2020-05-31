package com.ft.br.model.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 校验类
 *
 * @author shichunyang
 */
@Data
public class ValidChildren {
    @NotBlank(message = "activityDetailName 不为空")
    private String activityDetailName;
}
