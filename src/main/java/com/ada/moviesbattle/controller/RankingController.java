package com.ada.moviesbattle.controller;

import com.ada.moviesbattle.service.interfaces.IRankingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Ranking Controller", description = "Controller for ranking users by their scores")
@RestController
@RequestMapping("/ranking")
public class RankingController {
    private final IRankingService rankingService;

    public RankingController(IRankingService rankingService) {
        this.rankingService = rankingService;
    }

    @Operation(summary = "Get all users ranking",
            description = "Get all users ranking by their scores",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Users ranking retrieved successfully"),
                    @ApiResponse(responseCode = "500", description = "Server error")
            })
    @GetMapping
    public ResponseEntity getAllRanking() {
        return ResponseEntity.ok(rankingService.getAllRanking());
    }
}
