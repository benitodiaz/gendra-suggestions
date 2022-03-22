package com.example.demo.daos;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.example.demo.entities.City;

public interface ICitiesDao extends CrudRepository<City, Long>{
	
	public List<City> findByNameContainingIgnoreCase(String name);

}
