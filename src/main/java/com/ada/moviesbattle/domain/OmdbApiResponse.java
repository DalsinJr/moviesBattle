package com.ada.moviesbattle.domain;

import com.ada.moviesbattle.domain.dto.MovieDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class OmdbApiResponse {
    private String imdbID;
    @JsonProperty("Title")
    private String title;
    private Double imdbRating;
    private String imdbVotes;
    public static MovieDTO toMovie(OmdbApiResponse response) {
        return new MovieDTO(response.getImdbID(),
                response.getTitle(),
                response.getImdbRating() * Long.parseLong(response.getImdbVotes().replace(",", "")));
    }

}