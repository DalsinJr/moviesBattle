package com.ada.moviesbattle.service;

import com.ada.moviesbattle.domain.dto.MatchDTO;
import com.ada.moviesbattle.domain.dto.MovieDTO;
import com.ada.moviesbattle.domain.entity.MatchEntity;
import com.ada.moviesbattle.repository.MatchRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MatchService {


    private final MatchRepository matchRepository;

    private final MovieService movieService;

    private final RankingService rankingService;

    public MatchService(MatchRepository matchRepository, MovieService movieService, RankingService rankingService) {
        this.matchRepository = matchRepository;
        this.movieService = movieService;
        this.rankingService = rankingService;
    }

    public String setValidMatch(Object user) {
        MatchEntity match = new MatchEntity();
        match.setUserId(user.toString());
        match.setCorrectScoreCount(10D);
        match.setCurrentErrorCount(0);
        matchRepository.save(match);
        return "Match";
    }

    public Object getValidMatch(String user) {
        Optional<MatchEntity> matchByUserId = matchRepository.findByUserId(user);
        if (matchByUserId.isPresent()) {
            MatchEntity oldMatch = matchByUserId.get();
            return buildFromEntity(oldMatch);
        }
        MatchEntity newMatch = new MatchEntity(user, new ArrayList<>(movieService.generateUniquePair()));
        matchRepository.save(newMatch);
        return buildFromEntity(newMatch);
    }

    public MatchDTO buildFromEntity(MatchEntity matchEntity) {
        return MatchDTO.builder()
                .username(matchEntity.getUserId())
                .currentMatchMovies(movieService.getMovies(matchEntity.getCurrentMatchMoviesIds()))
                .correctScoreCount(matchEntity.getCorrectScoreCount())
                .currentErrorCount(matchEntity.getCurrentErrorCount())
                .build();
    }

    public Object userWinnerPrediction(String user, String movieId) {
        Optional<MatchEntity> matchByUserId = matchRepository.findByUserId(user);
        if (matchByUserId.isEmpty()) {
            return "Match not found";
        }
        MatchEntity match = matchByUserId.get();
        List<String> currentMatchMoviesIds = match.getCurrentMatchMoviesIds();
        if (!currentMatchMoviesIds.contains(movieId)) {
            return "Invalid Movie";
        }
        MovieDTO winner = buildFromEntity(match).currentMatchMovies().stream()
                .max(Comparator.comparing(MovieDTO::weightedRating))
                .orElseThrow( NoSuchElementException::new);

        if (winner.imdbId().equals(movieId)) {
            match.setCorrectScoreCount(match.getCorrectScoreCount() + 1);
        } else {
            match.setCurrentErrorCount(match.getCurrentErrorCount() + 1);
            if (match.getCurrentErrorCount() >= 3) {
                createScoreAndDeleteCurrentlyMatch(match);
                return "Match finished";
            }
        }
        match.setCurrentMatchMoviesIds(new ArrayList<>(movieService.generateUniquePair()));
        matchRepository.save(match);
        return "boora";
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
        Optional<MatchEntity> matchByUserId = matchRepository.findByUserId(user);
        matchByUserId.ifPresent(this::createScoreAndDeleteCurrentlyMatch);
    }


}
