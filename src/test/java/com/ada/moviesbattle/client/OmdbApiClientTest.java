package com.ada.moviesbattle.client;

import com.ada.moviesbattle.domain.OmdbApiResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@SpringBootTest
@ActiveProfiles("test")
public class OmdbApiClientTest {

    @Autowired
    private OmdbApiClient omdbApiClient;

    @Autowired
    private RestTemplate restTemplate;

    private MockRestServiceServer mockServer;

    @Value("${omdb.api.key-test}")
    private String apiKey;

    @BeforeEach
    public void setUp() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    @DisplayName("Test Fetch Movie By Id")
    public void testFetchMovieById() {
        String imdbId = "tt0111161";
        mockServer.expect(requestTo(String.format("http://www.omdbapi.com/?i=%s&apikey=%s", imdbId, apiKey)))
                .andRespond(withSuccess("{\"Title\":\"The Shawshank Redemption\",\"Year\":\"1994\",\"imdbID\":\"tt0111161\"}", MediaType.APPLICATION_JSON));

        OmdbApiResponse response = omdbApiClient.fetchMovieById(imdbId);
        assertNotNull(response);
        mockServer.verify();
    }
    @Test
    @DisplayName("Test Fetch Movie By Invalid Id")
    public void testFetchMovieByIdWithInvalidId() {
        String invalidId = "invalidId";
        mockServer.expect(requestTo(String.format("http://www.omdbapi.com/?i=%s&apikey=%s", invalidId, apiKey)))
                .andRespond(withStatus(NOT_FOUND));

        assertThrows(RestClientException.class, () -> omdbApiClient.fetchMovieById(invalidId));
        mockServer.verify();
    }


}