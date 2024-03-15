package com.ada.moviesbattle.repository;

import com.ada.moviesbattle.domain.entity.MatchEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface MatchRepository extends JpaRepository<MatchEntity, Long>{
    Optional<MatchEntity> findByUserId(String userId);
}
