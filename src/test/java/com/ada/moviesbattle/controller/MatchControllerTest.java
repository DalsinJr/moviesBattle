package com.ada.moviesbattle.controller;

import com.ada.moviesbattle.domain.dto.MatchDTO;
import com.ada.moviesbattle.domain.dto.MovieDTO;
import com.ada.moviesbattle.service.MatchService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class MatchControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MatchService matchService;

    // Example Test Cases
    @Test
    @WithMockUser(username="user1")
    public void getValidMatchTest() throws Exception {
        // Given
        given(matchService.getValidMatch()).willReturn(MatchDTO.builder()
                .username("user1")
                .currentMatchMovies(List.of(
                        new MovieDTO("movieId1", "Movie Title 1", 8.5),
                        new MovieDTO("movieId2", "Movie Title 2", 9.0)
                ))
                .correctScoreCount(0D)
                .currentErrorCount(0)
                .build());

        // When & Then
        mockMvc.perform(get("/match")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username="user1")
    public void userWinnerPredictionTest() throws Exception {
        // Given
        String movieId = "movie123";
        given(matchService.userWinnerPrediction(movieId)).willReturn("Valid Answer! Next match started!");

        // When & Then
        mockMvc.perform(post("/match/{movieId}", movieId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username="user1")
    public void finishMatchTest() throws Exception {

        // When & Then
        mockMvc.perform(post("/match/finish")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
