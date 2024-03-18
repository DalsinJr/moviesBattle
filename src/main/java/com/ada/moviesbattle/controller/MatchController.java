package com.ada.moviesbattle.controller;

import com.ada.moviesbattle.service.interfaces.IMatchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Match", description = "Match Controller")
@RestController
public class MatchController {

    private final IMatchService matchService;

    public MatchController(IMatchService matchService) {
        this.matchService = matchService;
    }


    @Operation(summary = "Get a valid match",
    responses = {
            @ApiResponse(responseCode = "200", description = "Match retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "All possible pairs have been used"),
            @ApiResponse(responseCode = "500", description = "Server error")
    })
    @GetMapping("/match")
    public ResponseEntity getValidMatch() {
        return ResponseEntity.ok(matchService.getValidMatch());
    }

    @Operation(summary = "Receive the chosen movieId as the highest rated movie of the currently match",
    responses = {
            @ApiResponse(responseCode = "200", description = "Match set successfully"),
            @ApiResponse(responseCode = "404", description = "Match not found"),
            @ApiResponse(responseCode = "409", description = "The chosen movie is not part of the current match"),
            @ApiResponse(responseCode = "500", description = "Server error")
    })
    @PostMapping("/match/{movieId}")
    public ResponseEntity userWinnerPrediction(@PathVariable String movieId) {
        return ResponseEntity.ok(matchService.userWinnerPrediction(movieId));
    }

    @PostMapping("match/finish")
    public ResponseEntity finishMatch() {
        matchService.finishMatch();
        return ResponseEntity.ok().build();
    }
}
