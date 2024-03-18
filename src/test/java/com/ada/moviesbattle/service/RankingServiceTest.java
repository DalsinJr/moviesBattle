package com.ada.moviesbattle.service;

import com.ada.moviesbattle.domain.dto.RankingDTO;
import com.ada.moviesbattle.domain.entity.RankingEntity;
import com.ada.moviesbattle.repository.RankingRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
public class RankingServiceTest {

    @Autowired
    private RankingService rankingService;

    @MockBean
    private RankingRepository rankingRepository;


    @Test
    public void testCreate() {
        // Given
        String username = "testUser";
        Double score = 10.0;

        // When
        rankingService.create(username, score);

        // Then
        verify(rankingRepository, times(1)).save(any(RankingEntity.class));
    }
    @Test
    public void testGetAllRanking() {
        List<RankingDTO> expectedRankings = IntStream.range(0, 10)
                .mapToObj(i -> new RankingDTO("user" + i, (double) i))
                .collect(Collectors.toList());

        when(rankingRepository.findAllByOrderByScoreDesc()).thenReturn(expectedRankings.stream()
                .map(dto -> new RankingEntity(dto.username(), dto.score()))
                .collect(Collectors.toList()));

        List<RankingDTO> actualRankings = rankingService.getAllRanking();

        assertEquals(expectedRankings, actualRankings);
    }
}