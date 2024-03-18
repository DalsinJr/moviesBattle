package com.ada.moviesbattle.client;

import com.ada.moviesbattle.domain.OmdbApiResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class OmdbApiClient {

    private final RestTemplate restTemplate;

    @Value("${omdb.api.key}")
    private String apiKey;

    @Value("${omdb.api.url}")
    private String apiUrl;

    public OmdbApiClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public OmdbApiResponse fetchMovieById(String id) {
       return restTemplate.getForObject(apiUrl + "?i={id}&apikey={apiKey}", OmdbApiResponse.class, id, apiKey);
    }

}
