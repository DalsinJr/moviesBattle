package com.ada.moviesbattle.controller;

import com.ada.moviesbattle.domain.entity.RankingEntity;
import com.ada.moviesbattle.repository.RankingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import java.util.List;


@SpringBootTest
@AutoConfigureMockMvc
public class RankingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RankingRepository rankingRepository;

    @BeforeEach
    public void setup() {
        rankingRepository.deleteAll();
        RankingEntity ranking1 = new RankingEntity( "testUser1", 100.0);
        RankingEntity ranking2 = new RankingEntity( "testUser2", 90.0);
        rankingRepository.saveAll(List.of(ranking1, ranking2));
    }

    @Test
    @WithMockUser
    public void getAllRankingTest() throws Exception {
        // When & Then
        mockMvc.perform(get("/ranking")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username", is(notNullValue())))
                .andExpect(jsonPath("$[0].score", is(notNullValue())));
    }
}