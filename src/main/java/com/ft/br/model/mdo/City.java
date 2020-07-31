package com.ft.br.model.mdo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * City
 *
 * @author shichunyang
 */
@Getter
@Setter
@ToString
public class City {

    public static final String PREFIX = "customize.cities";

    private Integer id;

    private String name;

    private String description;

    public City(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }
}
