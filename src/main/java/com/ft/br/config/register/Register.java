package com.ft.br.config.register;

import org.springframework.beans.factory.annotation.Autowired;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Register
 *
 * @author shichunyang
 * Created by shichunyang on 2020/7/31.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Autowired
public @interface Register {

    int age();

    String name();
}
