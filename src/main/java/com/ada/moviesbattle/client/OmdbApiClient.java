package com.ada.moviesbattle.client;

import com.ada.moviesbattle.domain.OmdbApiResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Component
public class OmdbApiClient {

    private final RestTemplate restTemplate;

    @Value("${omdb.api.key}")
    private String apiKey;

    @Value("${omdb.api.url}")
    private String apiUrl;

    public static List<String> IMDB_IDS = Arrays.asList(
            "tt0050083", "tt0060196", "tt0068646", "tt0071562", "tt0108052",
            "tt0110912", "tt0111161", "tt0111162", "tt0167260", "tt0468569"
    );
    private static final Random random = new Random();

    public static String getRandomImdbId() {
        int index = random.nextInt(IMDB_IDS.size());
        return IMDB_IDS.get(index);
    }

    public OmdbApiClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public OmdbApiResponse fetchMovieById(String id) {
       return restTemplate.getForObject(apiUrl + "?i={id}&apikey={apiKey}", OmdbApiResponse.class, id, apiKey);
    }

}
