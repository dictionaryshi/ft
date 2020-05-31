package com.ft.br.model.mdo;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;

/**
 * Province
 *
 * @author shichunyang
 */
@Data
public class Province {
    private Integer id;

    private String name;

    private Set<City> cities = new HashSet<>();

    public Province(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public void addCity(City city) {
        cities.add(city);
    }
}
