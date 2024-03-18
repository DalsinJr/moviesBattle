package com.ada.moviesbattle.service;

import com.ada.moviesbattle.domain.dto.MatchDTO;
import com.ada.moviesbattle.domain.dto.MovieDTO;
import com.ada.moviesbattle.domain.entity.MatchEntity;
import com.ada.moviesbattle.repository.MatchRepository;
import com.ada.moviesbattle.security.utils.SecurityUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
public class MatchServiceTest {

    @Autowired
    private MatchService matchService;

    @MockBean
    private MatchRepository matchRepository;

    @MockBean
    private MovieService movieService;

    @MockBean
    private RankingService rankingService;

    @Test
    @WithMockUser(username = "user1")
    public void testGetValidMatch_NewMatch() {
        //Given
        String loggedUser = SecurityUtils.getLoggedUser();
        when(matchRepository.findByUserId(loggedUser)).thenReturn(Optional.empty());
        Set<String> movieIds = Set.of("tt0111161", "tt0068646");
        when(movieService.generateUniquePair()).thenReturn( movieIds);

        // When
        MatchDTO matchDTO = matchService.getValidMatch();

        // Then
        verify(matchRepository, times(1)).findByUserId(loggedUser);
        verify(matchRepository, times(1)).save(any(MatchEntity.class));
        assertNotNull(matchDTO);
        assertEquals(loggedUser, matchDTO.username());
        assertTrue(matchDTO.currentMatchMovies().stream().map(MovieDTO::imdbId).allMatch(movieIds::contains));
    }

    @Test
    @WithMockUser(username = "user1")
    public void testUserWinnerPrediction_CorrectPrediction() {
        //Given
        String loggedUser = SecurityUtils.getLoggedUser();
        String correctMovieId = "movie1";
        List<String> currentMatchMoviesIds = List.of(correctMovieId, "movie2");

        MatchEntity matchEntity = new MatchEntity(loggedUser, currentMatchMoviesIds);

        when(matchRepository.findByUserId(loggedUser)).thenReturn(Optional.of(matchEntity));

        MovieDTO winnerMovie = new MovieDTO("movie1", "Title 1", 8.5);
        when(movieService.getMovies(anyList())).thenReturn(List.of(winnerMovie, new MovieDTO("movie2", "Title 2", 8.0)));
        when(movieService.generateUniquePair()).thenReturn(Set.of("movie3", "movie4"));

        // When
        String result = matchService.userWinnerPrediction(correctMovieId);

        // Then
        verify(matchRepository, times(1)).save(matchEntity);
        assertEquals("Valid Answer! Next match started!", result);
        assertEquals(1, matchEntity.getCorrectScoreCount());
    }

    @Test
    @WithMockUser(username = "user1")
    public void testFinishMatch() {
        String loggedUser = SecurityUtils.getLoggedUser();
        MatchEntity matchEntity = new MatchEntity();
        matchEntity.setUserId(loggedUser);
        matchEntity.setCorrectScoreCount(5D);
        matchEntity.setCurrentErrorCount(1);

        when(matchRepository.findByUserId(loggedUser)).thenReturn(Optional.of(matchEntity));

        // Execute
        matchService.finishMatch();

        // Verify interactions
        verify(matchRepository, times(1)).delete(matchEntity);
        verify(rankingService, times(1)).create(eq(loggedUser), anyDouble());
    }


}