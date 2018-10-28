package com.ft.xml;

import lombok.Data;

/**
 * City
 *
 * @author shichunyang
 */
@Data
public class City {
	private Integer id;

	private String name;

	private String description;

	public City(int id, String name, String description) {
		this.id = id;
		this.name = name;
		this.description = description;
	}
}
