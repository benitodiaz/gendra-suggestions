package com.example.demo.services.implement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.demo.daos.ICitiesDao;
import com.example.demo.entities.City;
import com.example.demo.models.Suggestion;
import com.example.demo.utils.Utils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class SuggestionsService implements ISuggestionsService {
	
	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	private static final String GEOCODE_URL = "https://geocode.maps.co/search?q=";

	@Autowired
	private ICitiesDao citiesDao;

	@Override
	public List<Suggestion> findByNameLike(String name) {
		List<City> citiesDB = citiesDao.findByNameContainingIgnoreCase(name);
		List<Suggestion> suggestions = new ArrayList<>();
		log.info("The cities have been obtained from database. Size: " + citiesDB.size());

		if(citiesDB.stream().filter(cityDB -> cityDB.getName().equals(name)).count() == 0) {	
			City cityToSearchByName = City.builder()
					.name(name).build();
			citiesDB.add(cityToSearchByName);
		}
		
		for(City cityDB : citiesDB){
			
			RestTemplate template = new RestTemplate();
			 
	        String jsonString = template.getForObject(GEOCODE_URL + cityDB.getName(), String.class);
	        ObjectMapper mapper = new ObjectMapper();
	        try {
				
				List<Map<String, Object>> list = mapper.readValue(jsonString, new TypeReference<List<Map<String, Object>>>(){});
				
				if(list.size() == 0) {
					continue;
				}
				
				for(Map<String, Object> element: list){
					
					try {
						
						City cityApi = City.builder()
								.name(element.get("display_name").toString())
								.country("")
								.latitude(Double.parseDouble(element.get("lat").toString()))
								.longitude(Double.parseDouble(element.get("lon").toString()))
								.build();
						
				        Suggestion suggestion = new Suggestion(cityApi);
				        suggestion.setScore(0.0);
				        suggestions.add(suggestion);
						
					} catch (NumberFormatException e) {
						
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
			
		}
		return suggestions;
	}

	@Override
	public List<Suggestion> findByNameLikeAndWithCoordinates(String name, String latitude, String longitude) {
		
		City cityToCompareByLocation = City.builder()
				.latitude(Double.valueOf(latitude))
				.longitude(Double.valueOf(longitude)).build();
		
		double maxDistance = 0;
		
		List<City> citiesDB = citiesDao.findByNameContainingIgnoreCase(name);
		log.info("The cities have been obtained from database. Size: " + citiesDB.size());
		
		if(citiesDB.stream().filter(cityDB -> cityDB.getName().equals(name)).count() == 0) {			
			City cityToSearchByName = City.builder()
					.name(name).build();
			citiesDB.add(cityToSearchByName);
		}
		
		List<Suggestion> suggestions = new ArrayList<>();
		for(City city: citiesDB) {
			
			RestTemplate template = new RestTemplate();
			
	        String jsonString = template.getForObject(GEOCODE_URL + city.getName()/*.split("\s")[0]*/, String.class);
	        ObjectMapper mapper = new ObjectMapper();
	        try {
				
				List<Map<String, Object>> list = mapper.readValue(jsonString, new TypeReference<List<Map<String, Object>>>(){});
				
				if(list.size() == 0) {
					continue;
				}
				
				for(Map<String, Object> element: list){
					
					try {
						City cityApi = City.builder()
								.name(element.get("display_name").toString())
								.country("")
								.latitude(Double.parseDouble(element.get("lat").toString()))
								.longitude(Double.parseDouble(element.get("lon").toString()))
								.build();
						Double elementDistance = Utils.geoDistance(cityApi, cityToCompareByLocation);
						if(elementDistance > maxDistance) {
							maxDistance = elementDistance;
						}
						
						suggestions.add(new Suggestion(cityApi));
					} catch (NumberFormatException e) {
						
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
			
		}
		
		for(Suggestion suggestion: suggestions) {
			double score = (maxDistance - Utils.geoDistance(suggestion, cityToCompareByLocation)) / maxDistance;
			score = (int)(100*score)/100.0;
			suggestion.setScore(score);
		}
		
		Collections.sort(suggestions, (a,b) -> a.getScore() > b.getScore() ? -1 : 1);

		return suggestions;
	}

}
