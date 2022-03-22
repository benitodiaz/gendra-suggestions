package com.example.demo.utils;

import com.example.demo.entities.City;
import com.example.demo.models.Suggestion;

public class Utils {
	public static final Double EARTH_RADIUS = 6371.0;
	
	public static boolean areValidLatitudeAndLongitude(String latitudeString, String longitudeString) {
		try {
			Double latitude = Double.parseDouble(latitudeString);
			Double longitude = Double.parseDouble(longitudeString);
			if(-90.0<=latitude && latitude<=90.0 && -180.0<=longitude && longitude<=180.0) {
				return true;
			}else {
				return false;
			}
		}catch(Exception e) {
			return false;
		}
	}
	
	public static Double geoDistance(Double lat1Deg, Double lon1Deg, Double lat2Deg, Double lon2Deg) {
		
		try {
			
			Double lat1 = Math.toRadians(lat1Deg);
			Double lon1 = Math.toRadians(lon1Deg);
			Double lat2 = Math.toRadians(lat2Deg);
			Double lon2 = Math.toRadians(lon2Deg);
			
			Double dLon = lon2 - lon1;
			Double dLat = lat2 - lat1;
			
			Double a = Math.pow(Math.sin(dLat/2), 2.0) 
					+ Math.cos(lat1)*Math.cos(lat2)*Math.pow(Math.sin(dLon/2), 2);
			Double c = 2*Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
			
			return EARTH_RADIUS * c;
		}catch(Exception e) {
			return -1.0;
		}
		
	}
	
	public static Double geoDistance(City city1, City city2) {
		
		return geoDistance(city1.getLatitude(), city1.getLongitude(), 
				city2.getLatitude(), city2.getLongitude());

	}
	
	public static Double geoDistance(Suggestion suggestion, City city) {
		
		return geoDistance(suggestion.getLatitude(), suggestion.getLongitude(), city.getLatitude(), city.getLongitude());
		
	}
	
	public static Double geoDistance(Suggestion suggestion1, Suggestion suggestion2) {
		
		return geoDistance(suggestion1.getLatitude(), suggestion1.getLongitude(), 
				suggestion2.getLatitude(), suggestion2.getLongitude());
		
	}

}
