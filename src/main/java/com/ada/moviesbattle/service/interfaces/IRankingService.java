package com.ada.moviesbattle.service.interfaces;

import com.ada.moviesbattle.domain.dto.RankingDTO;

import java.util.List;

public interface IRankingService {
    void create(String username, Double score);
    List<RankingDTO> getAllRanking();
}