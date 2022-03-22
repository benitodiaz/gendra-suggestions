package com.example.demo.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.services.implement.ISuggestionsService;
import com.example.demo.utils.Utils;

@RestController
@RequestMapping("/suggestions")
public class SuggestionsController {
	
	private Logger log = LoggerFactory.getLogger(SuggestionsController.class);

	@Autowired
	private ISuggestionsService suggestionsService;
	
	@GetMapping({"/", ""})
	public Object suggest(@RequestParam(name="q") String q, 
			@RequestParam(name="latitude", defaultValue="") String latitude, 
			@RequestParam(name="longitude", defaultValue="") String longitude) {
		
	    try {
			if(latitude.equals("") && longitude.equals("")) {		
				log.info("Will return search only by name");
				return suggestionsService.findByNameLike(q);
			}
			if(!Utils.areValidLatitudeAndLongitude(latitude,longitude)) {
				log.error("Invalid coordinates");
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
			}
			log.info("Will search by name and coordinates");
			return suggestionsService.findByNameLikeAndWithCoordinates(q, latitude, longitude);
	    } catch (NumberFormatException e) {
	        throw  new ResponseStatusException(HttpStatus.BAD_REQUEST);
	    }catch (ResponseStatusException e) {
	        throw  new ResponseStatusException(HttpStatus.BAD_REQUEST);
	    }catch (Exception e){
	        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}
	
}
