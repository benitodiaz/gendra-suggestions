package com.example.demo.models;

import com.example.demo.entities.City;

import lombok.Data;

@Data
@SuppressWarnings("unused")
public class Suggestion {

	private String name;
	private Double latitude;
	private Double longitude;
	private Double score;
	
	public Suggestion(City city) {
		this.name = city.getName() + (city.getCountry() != null ? ", " + city.getCountry() : "");
		this.latitude = city.getLatitude();
		this.longitude = city.getLongitude();
	}

}
