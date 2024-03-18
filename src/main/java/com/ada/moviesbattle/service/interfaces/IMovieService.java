package com.ada.moviesbattle.service.interfaces;

import com.ada.moviesbattle.domain.dto.MovieDTO;

import java.util.List;
import java.util.Set;

public interface IMovieService {
    List<MovieDTO> getMovies(List<String> moviesIds);
    Set<String> generateUniquePair();
}
