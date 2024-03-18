package com.ada.moviesbattle.service;

import com.ada.moviesbattle.client.OmdbApiClient;
import com.ada.moviesbattle.domain.OmdbApiResponse;
import com.ada.moviesbattle.domain.dto.MovieDTO;
import com.ada.moviesbattle.util.MovieUtils;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
public class MovieServiceTest {

    @Autowired
    MovieService movieService;

    @Mock
    private OmdbApiClient omdbApiClient;


    @Test
    public void testFetchMovieDetails() {
        // Given
        List<String> pair = Arrays.asList("tt0111161", "tt0068646");
        OmdbApiResponse sonhoDeLiberdade = new OmdbApiResponse("tt0111161","The Shawshank Redemption",9.3D,"2,865,799");
        OmdbApiResponse oPoderosoChefao = new OmdbApiResponse("tt0068646","The Godfather",9.2D,"1,611,542");
        when(omdbApiClient.fetchMovieById(anyString())).thenReturn(sonhoDeLiberdade, oPoderosoChefao);

        // When
        List<MovieDTO> movies = movieService.getMovies(pair);

        // Then
        assertEquals(2, movies.size());
        assertEquals("tt0111161", movies.get(0).imdbId());
        assertEquals("tt0068646", movies.get(1).imdbId());
        assertEquals("The Shawshank Redemption", movies.get(0).title());
        assertEquals("The Godfather", movies.get(1).title());
    }

    @Test
    public void testGenerateUniquePairThrowsExceptionWhenAllPairsAreUsed() {
        // Given
        ReflectionTestUtils.setField(movieService, "usedPairs", new HashSet<>());
        List<String> mockedImdbIds = Arrays.asList("tt0111161", "tt0068646", "tt0167260");
        ReflectionTestUtils.setField(MovieUtils.class, "IMDB_IDS", mockedImdbIds);

        // When
        movieService.generateUniquePair();
        movieService.generateUniquePair();
        movieService.generateUniquePair();

        // Then
        assertThrows(IllegalStateException.class, () -> movieService.generateUniquePair());
    }
}