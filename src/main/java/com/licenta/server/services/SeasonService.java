package com.licenta.server.services;

import com.licenta.server.TMDBStuff.TmdbClient;
import com.licenta.server.TMDBStuff.TmdbSeasonDto;
import com.licenta.server.dto.SeasonDto;
import com.licenta.server.mapper.SeasonMapper;
import com.licenta.server.models.Season;
import com.licenta.server.repository.SeasonRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class SeasonService {
    private final SeasonRepository seasonRepository;
    private final TmdbClient tmdbClient;
    public SeasonMapper seasonMapper;

    public TmdbSeasonDto fetchSeasonFromTmdb(int seriesId , int seasonNumber){
        return tmdbClient.getSeasonDetails(seriesId,seasonNumber);
    }

    public SeasonDto getSeasonDetails(int seriesId , int seasonNumber){
        return seasonRepository.findSeasonBySeriesIdAndSeasonNumber(seriesId,seasonNumber).map(SeasonMapper::mapToDto)
                .orElseGet(() -> seasonMapper.mapTmdbSeasonToDto(fetchSeasonFromTmdb(seriesId,seasonNumber)));
    }

    @Transactional
    public Season upsertAndSyncSeason(int seriesId, int seasonNumber){
        Season season = seasonRepository.findSeasonBySeriesIdAndSeasonNumber(seriesId,seasonNumber)
                .orElseGet(Season::new);
        season.setSeriesId(seriesId);

        TmdbSeasonDto dto = tmdbClient.getSeasonDetails(seriesId , seasonNumber);

        season.setName(dto.getName());
        season.setSeasonNumber(dto.getSeasonNumber());
        season.setOverview(dto.getOverview());
        season.setVoteAverage(dto.getVoteAverage());
        season.setAirDate(dto.getAirDate());
        season.setPosterPath(dto.getPosterPath());
        season.setLastSyncedAt(Instant.now());
        return seasonRepository.save(season);

    }
}
