package com.ada.moviesbattle.repository;

import com.ada.moviesbattle.domain.entity.RankingEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RankingRepository extends JpaRepository<RankingEntity, Long> {

     List<RankingEntity> findAllByOrderByScoreDesc();
}
