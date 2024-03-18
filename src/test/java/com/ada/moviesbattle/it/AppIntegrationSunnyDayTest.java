package com.ada.moviesbattle.it;

import com.ada.moviesbattle.domain.dto.MatchDTO;
import com.ada.moviesbattle.security.domain.dto.AuthDTO;
import com.ada.moviesbattle.security.domain.dto.LoginResponseDTO;
import com.ada.moviesbattle.security.domain.dto.UserDTO;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AppIntegrationSunnyDayTest {

    public static final String LOCAL_TEST_URL = "http://localhost";
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String authToken;

    private String imdbIdFromResponse;

    @Test
    @Order(1)
    public void testRegister() {
        String url = LOCAL_TEST_URL + ":" + port + "/auth/register";
        UserDTO userDTO = new UserDTO("newUser", "newPassword");
        ResponseEntity<Object> response = restTemplate.postForEntity(url, userDTO, Object.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }


    @Test
    @Order(2)
    public void testLogin() {
        String url = LOCAL_TEST_URL + ":" + port + "/auth/login";
        AuthDTO authDTO = new AuthDTO("newUser", "newPassword");
        ResponseEntity<LoginResponseDTO> response = restTemplate.postForEntity(url, authDTO, LoginResponseDTO.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        LoginResponseDTO loginResponse =  response.getBody();
        assertThat(loginResponse).isNotNull();
        authToken = loginResponse.token();
    }

    @Test
    @Order(3)
    public void testGetValidMatch() {
        String url = LOCAL_TEST_URL + ":" + port + "/match";
        HttpHeaders headers = getAuthHeaders();
        HttpEntity<?> entity = new HttpEntity<>(headers);
        ResponseEntity<MatchDTO> response = restTemplate.exchange(url, HttpMethod.GET, entity, MatchDTO.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();

        MatchDTO match = response.getBody();
        imdbIdFromResponse = match.currentMatchMovies().get(0).imdbId();



    }

    @Test
    @Order(4)
    public void testUserWinnerPrediction() {
        String url = LOCAL_TEST_URL + ":" + port + "/match/" + imdbIdFromResponse;
        HttpHeaders headers = getAuthHeaders();
        HttpEntity<?> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

    }

    @Test
    @Order(5)
    public void testFinishMatch() {
        String url = "http://localhost:" + port + "/match/finish";
        HttpHeaders headers = getAuthHeaders();
        HttpEntity<?> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }



    private HttpHeaders getAuthHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(authToken);
        return headers;
    }
}
