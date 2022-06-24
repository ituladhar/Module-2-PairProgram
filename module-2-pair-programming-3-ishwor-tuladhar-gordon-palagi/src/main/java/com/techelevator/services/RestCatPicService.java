package com.techelevator.services;

import com.techelevator.model.CatFact;
import org.springframework.stereotype.Component;

import com.techelevator.model.CatPic;
import org.springframework.web.client.RestTemplate;

@Component
public class RestCatPicService implements CatPicService {

	@Override
	public CatPic getPic() {
		// TODO Auto-generated method stub
		RestTemplate restTemplate = new RestTemplate();
		String url="https://cat-data.netlify.app/api/pictures/random";
		CatPic image = restTemplate.getForObject(url, CatPic.class);
		return image;

	}

}	
