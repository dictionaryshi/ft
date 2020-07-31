package com.ft.br.config.register;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * Created by shichunyang on 2020/7/31.
 */
@Getter
@Setter
@ToString
public class Acomponent {

    private Integer age;

    private String name;

    private Date createdAt;

    private Bcomponent bcomponent;

    public void init() {
        System.out.println("A init");
    }

    public void destroy() {
        System.out.println("A destroy");
    }
}
