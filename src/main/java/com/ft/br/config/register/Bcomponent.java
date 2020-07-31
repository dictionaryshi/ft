package com.ft.br.config.register;

import com.ft.br.service.GoodsService;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by shichunyang on 2020/7/31.
 */
@Getter
@Setter
@ToString
public class Bcomponent {

    private GoodsService goodsService;

    public void init() {
        System.out.println("B init");
    }

    public void destroy() {
        System.out.println("B destroy");
    }
}
