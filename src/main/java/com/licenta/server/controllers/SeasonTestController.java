package com.licenta.server.controllers;


import com.licenta.server.dto.SeasonDto;
import com.licenta.server.mapper.SeasonMapper;
import com.licenta.server.models.Season;
import com.licenta.server.repository.SeasonRepository;
import com.licenta.server.services.SeasonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/test/seasons")
@RequiredArgsConstructor
public class SeasonTestController {

    private final SeasonService seasonService;
    private final SeasonRepository seasonRepository;

    /**
     * Returns season details:
     * - If present in DB -> returns mapped SeasonDto
     * - Else -> fetches from TMDB and returns mapped SeasonDto (does NOT necessarily persist)
     */
    @GetMapping("/{seriesId}/{seasonNumber}")
    public ResponseEntity<SeasonDto> getSeasonDetails(
            @PathVariable int seriesId,
            @PathVariable int seasonNumber
    ) {
        SeasonDto dto = seasonService.getSeasonDetails(seriesId, seasonNumber);
        return ResponseEntity.ok(dto);
    }

    /**
     * Forces a TMDB sync + upsert into DB, then returns the saved season as DTO.
     */
    @PostMapping("/{seriesId}/{seasonNumber}/sync")
    public ResponseEntity<SeasonDto> syncSeason(
            @PathVariable int seriesId,
            @PathVariable int seasonNumber
    ) {
        Season saved = seasonService.upsertAndSyncSeason(seriesId, seasonNumber);
        return ResponseEntity.ok(SeasonMapper.mapToDto(saved));
    }

    /**
     * Simple DB existence check (optional).
     */
    @GetMapping("/{seriesId}/{seasonNumber}/exists")
    public ResponseEntity<Boolean> existsInDb(
            @PathVariable int seriesId,
            @PathVariable int seasonNumber
    ) {
        boolean exists = seasonRepository.existsBySeriesIdAndSeasonNumber(seriesId  , seasonNumber);
        return ResponseEntity.ok(exists);
    }
}