package com.ada.moviesbattle.domain.dto;

import lombok.Builder;

@Builder
public record MovieDTO(String imdbId,String title, Double weightedRating) {
}
