package com.ft.model.dto;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.List;

/**
 * 校验model类
 *
 * @author shichunyang
 */
@Data
public class ValidParent {
	@NotNull(message = "activityId 不为NULL")
	@DecimalMin(value = "1", inclusive = false, message = "活动id必须大于1")
	@DecimalMax(value = "100", message = "活动id必须小于等于100")
	private Integer activityId;

	@NotBlank(message = "activityName 不为空")
	@Size(min = 3, max = 8, message = "活动名称长度[3,8]")
	private String activityName;

	@Email(message = "email 邮件格式错误")
	private String email;

	@NotNull(message = "details 不为NULL")
	@Size(min = 1, message = "活动明细不能为空")
	private @Valid List<ValidChildren> details;
}
