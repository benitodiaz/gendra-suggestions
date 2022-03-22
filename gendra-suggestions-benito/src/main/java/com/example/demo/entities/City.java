package com.example.demo.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "CITIES")
@SuppressWarnings("unused")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class City {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	private String country;
	
	private String name;
	
	private Double latitude;
	private Double longitude;
	
//	public City(String name, Double latitude, Double longitude) {
//		this.name = name;
//		this.country = "";
//		this.latitude = latitude;
//		this.longitude = longitude;
//	}
}
