package com.ada.moviesbattle.domain.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record MatchDTO(String username, List<MovieDTO> currentMatchMovies, Double correctScoreCount, Integer currentErrorCount) {
}
