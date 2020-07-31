package com.ft.br.config.register;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by shichunyang on 2020/7/31.
 */
@Getter
@Setter
@ToString
public class Ccomponent {

    public void init() {
        System.out.println("C init");
    }

    public void destroy() {
        System.out.println("C destroy");
    }
}
