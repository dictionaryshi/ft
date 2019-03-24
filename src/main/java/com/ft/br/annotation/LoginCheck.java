package com.ft.br.annotation;

import java.lang.annotation.*;

/**
 * 登录校验标志
 *
 * @author shichunyang
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LoginCheck {
}
