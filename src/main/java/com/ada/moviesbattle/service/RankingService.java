package com.ada.moviesbattle.service;

import com.ada.moviesbattle.domain.dto.RankingDTO;
import com.ada.moviesbattle.domain.entity.RankingEntity;
import com.ada.moviesbattle.repository.RankingRepository;
import com.ada.moviesbattle.service.interfaces.IRankingService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RankingService implements IRankingService {
    private final RankingRepository rankingRepository;

    public RankingService(RankingRepository rankingRepository) {
        this.rankingRepository = rankingRepository;
    }

    public void create(String username, Double score){
        rankingRepository.save(new RankingEntity(username, score));
    }

    public List<RankingDTO> getAllRanking() {
        return rankingRepository.findAllByOrderByScoreDesc().stream()
                .map(RankingEntity::toDTO)
                .toList();
    }
}
