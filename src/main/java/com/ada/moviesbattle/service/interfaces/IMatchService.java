package com.ada.moviesbattle.service.interfaces;

import com.ada.moviesbattle.domain.dto.MatchDTO;

public interface IMatchService {
    MatchDTO getValidMatch();
    String userWinnerPrediction(String movieId);
    void finishMatch();
}
