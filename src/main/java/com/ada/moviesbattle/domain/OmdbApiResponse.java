package com.ada.moviesbattle.domain;

import com.ada.moviesbattle.domain.dto.MovieDTO;
import com.fasterxml.jackson.annotation.JsonProperty;

public record OmdbApiResponse(
    String imdbID,
    @JsonProperty("Title") String title,
    Double imdbRating,
    String imdbVotes) {


    public static MovieDTO toMovie(OmdbApiResponse response) {
        return new MovieDTO(response.imdbID(),
                response.title(),
                response.imdbRating() * Long.parseLong(response.imdbVotes().replace(",", "")));
    }
}