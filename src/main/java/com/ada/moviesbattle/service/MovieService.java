package com.ada.moviesbattle.service;

import com.ada.moviesbattle.client.OmdbApiClient;
import com.ada.moviesbattle.domain.OmdbApiResponse;
import com.ada.moviesbattle.domain.dto.MovieDTO;
import com.ada.moviesbattle.service.interfaces.IMovieService;
import com.ada.moviesbattle.util.MovieUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static com.ada.moviesbattle.util.MovieUtils.getRandomImdbId;

@Service
public class MovieService implements IMovieService {


    private final OmdbApiClient omdbApiClient;
    private final Set<Set<String>> usedPairs = new HashSet<>();

    public MovieService(OmdbApiClient omdbApiClient) {
        this.omdbApiClient = omdbApiClient;
    }

    public List<MovieDTO> getMovies(List<String> moviesIds) {
        Set<String> pair = getMoviePair(moviesIds);
        return fetchMovieDetails(pair);
    }

    public Set<String> generateUniquePair() {
        int imdbMockedSize = MovieUtils.IMDB_IDS.size();
        if (usedPairs.size() == imdbMockedSize * (imdbMockedSize - 1) / 2) {
            throw new IllegalStateException("All possible pairs have been used.");
        }
        Set<String> pair;
        do {
            String firstMovieId = getRandomImdbId();
            String secondMovieId = getRandomImdbId();
            while (firstMovieId.equals(secondMovieId)) {
                secondMovieId = getRandomImdbId();
            }
            pair = Set.of(firstMovieId, secondMovieId);
        } while (usedPairs.contains(pair));
        usedPairs.add(pair);
        return pair;
    }

    private Set<String> getMoviePair(List<String> moviesIds) {
        Set<String> pair = CollectionUtils.isEmpty(moviesIds) ? generateUniquePair() : new HashSet<>(moviesIds);

        if (pair == null) {
            throw new IllegalStateException("All possible pairs have been used.");
        }
        usedPairs.add(pair);
        return pair;
    }

    private List<MovieDTO> fetchMovieDetails(Set<String> pair) {
        Iterator<String> iterator = pair.iterator();
        OmdbApiResponse firstMovie = omdbApiClient.fetchMovieById(iterator.next());
        OmdbApiResponse secondMovie = omdbApiClient.fetchMovieById(iterator.next());

        return List.of(OmdbApiResponse.toMovie(firstMovie), OmdbApiResponse.toMovie(secondMovie));
    }


}
