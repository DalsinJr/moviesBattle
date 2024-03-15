package com.ada.moviesbattle.domain.entity;

import com.ada.moviesbattle.domain.dto.RankingDTO;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class RankingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String userId;
    private Double score;

    public RankingEntity(String username, Double score) {
        this.userId = username;
        this.score = score;
    }

    public static RankingDTO toDTO(RankingEntity rankingEntity) {
        return new RankingDTO(rankingEntity.userId, rankingEntity.score);
    }
}
