package com.example.demo.services.implement;

import java.util.List;

import com.example.demo.models.Suggestion;

public interface ISuggestionsService {
	
	public List<Suggestion> findByNameLike(String name);

	public List<Suggestion> findByNameLikeAndWithCoordinates(String name, String latitude, String longitude);
}
