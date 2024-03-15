package com.ada.moviesbattle.service;

import com.ada.moviesbattle.domain.dto.MatchDTO;
import com.ada.moviesbattle.domain.dto.MovieDTO;
import com.ada.moviesbattle.domain.entity.MatchEntity;
import com.ada.moviesbattle.repository.MatchRepository;
import com.ada.moviesbattle.security.utils.SecurityUtils;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MatchService {


    public static final int MAX_FAIL_ATTEMPTS = 3;
    private final MatchRepository matchRepository;

    private final MovieService movieService;

    private final RankingService rankingService;

    public MatchService(MatchRepository matchRepository, MovieService movieService, RankingService rankingService) {
        this.matchRepository = matchRepository;
        this.movieService = movieService;
        this.rankingService = rankingService;
    }

   public Object getValidMatch() {
    String loggedUser = SecurityUtils.getLoggedUser();
    MatchEntity currentlyMatch = matchRepository.findByUserId(loggedUser)
            .orElseGet(() -> createNewMatch(loggedUser));
    return buildFromEntity(currentlyMatch);
    }

    private MatchEntity createNewMatch(String user) {
        MatchEntity newMatch = new MatchEntity(user, new ArrayList<>(movieService.generateUniquePair()));
        matchRepository.save(newMatch);
        return newMatch;
    }

    public MatchDTO buildFromEntity(MatchEntity matchEntity) {
        return MatchDTO.builder()
                .username(matchEntity.getUserId())
                .currentMatchMovies(movieService.getMovies(matchEntity.getCurrentMatchMoviesIds()))
                .correctScoreCount(matchEntity.getCorrectScoreCount())
                .currentErrorCount(matchEntity.getCurrentErrorCount())
                .build();
    }

    public String userWinnerPrediction(String movieId) {
    String user = SecurityUtils.getLoggedUser();
    MatchEntity match = matchRepository.findByUserId(user)
            .orElseThrow(() -> new IllegalStateException("Match not found"));
    List<String> currentMatchMoviesIds = match.getCurrentMatchMoviesIds();
    if (!currentMatchMoviesIds.contains(movieId)) throw new NoSuchElementException("The chosen movie is not part of the current match");

    MovieDTO winner = findWinnerMovie(match);
    updateMatchScores(match, winner, movieId);
    handleMatchCompletion(match);

    match.setCurrentMatchMoviesIds(new ArrayList<>(movieService.generateUniquePair()));
    matchRepository.save(match);
    return "Valid Answer! Next match started!";
}

private MovieDTO findWinnerMovie(MatchEntity match) {
    return buildFromEntity(match).currentMatchMovies().stream()
            .max(Comparator.comparing(MovieDTO::weightedRating))
            .orElseThrow( NoSuchElementException::new);
}

private void updateMatchScores(MatchEntity match, MovieDTO winner, String movieId) {
    if (winner.imdbId().equals(movieId)) {
        match.setCorrectScoreCount(match.getCorrectScoreCount() + 1);
    } else {
        match.setCurrentErrorCount(match.getCurrentErrorCount() + 1);
    }
}

private void handleMatchCompletion(MatchEntity match) {
    if (match.getCurrentErrorCount() >= MAX_FAIL_ATTEMPTS) {
        createScoreAndDeleteCurrentlyMatch(match);
        throw new IllegalStateException("Match finished, Maximum attempts reached!");
    }
}

    private void createScoreAndDeleteCurrentlyMatch(MatchEntity match) {
        rankingService.create(match.getUserId(), calculateScore(match));
        matchRepository.delete(match);
    }

    public double calculateScore(MatchEntity match) {
        double totalQuizzes = match.getCorrectScoreCount() + match.getCurrentErrorCount();
        double correctPercentage = match.getCorrectScoreCount() / totalQuizzes;
        return totalQuizzes * correctPercentage;
    }
    public void finishMatch(String user) {
        matchRepository.findByUserId(user)
                .ifPresent(this::createScoreAndDeleteCurrentlyMatch);
    }


}
