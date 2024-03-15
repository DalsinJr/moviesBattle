package com.ada.moviesbattle.controller;

import com.ada.moviesbattle.service.MatchService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.context.SecurityContextHolder;

@RestController
public class MatchController {

    private final MatchService matchService;

    public MatchController(MatchService matchService) {
        this.matchService = matchService;
    }
    @PostMapping("/match")
    public String setValidMatch() {
        var user = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return matchService.setValidMatch(user);

    }
    @GetMapping("/match")
    public ResponseEntity getValidMatch() {
        String user = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(matchService.getValidMatch(user));
    }

    @PostMapping("/match/{movieId}")
    public ResponseEntity setMatch(@PathVariable String movieId) {
        String user = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(matchService.userWinnerPrediction (user, movieId));
    }

    @PostMapping("match/finish")
    public ResponseEntity finishMatch() {
        String user = SecurityContextHolder.getContext().getAuthentication().getName();
        matchService.finishMatch(user);
        return ResponseEntity.ok().build();
    }
}
