package com.techelevator.services;

import org.springframework.stereotype.Component;

import com.techelevator.model.CatFact;
import org.springframework.web.client.RestTemplate;

@Component
public class RestCatFactService implements CatFactService {

	@Override
	public CatFact getFact() {
		// TODO Auto-generated method stub
		RestTemplate restTemplate = new RestTemplate();
		String url="https://cat-data.netlify.app/api/facts/random";
		CatFact fact = restTemplate.getForObject(url, CatFact.class);
		return fact;
	}

}
