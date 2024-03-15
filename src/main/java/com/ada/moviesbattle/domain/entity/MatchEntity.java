package com.ada.moviesbattle.domain.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class MatchEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(unique = true)
    private String userId;
    private List<String> currentMatchMoviesIds;
    private Double correctScoreCount;
    private Integer currentErrorCount;

    public MatchEntity(String string, List<String> currentMatchMoviesIds) {
        this.userId = string;
        this.currentMatchMoviesIds = currentMatchMoviesIds;
        this.correctScoreCount = 0D;
        this.currentErrorCount = 0;
    }

}
