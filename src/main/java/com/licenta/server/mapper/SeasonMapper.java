package com.licenta.server.mapper;

import com.licenta.server.TMDBStuff.TmdbSeasonDto;
import com.licenta.server.dto.SeasonDto;
import com.licenta.server.models.Season;

public class SeasonMapper {

    public static SeasonDto mapTmdbSeasonToDto(TmdbSeasonDto dto){
        return SeasonDto.builder()
                .seasonNumber(dto.getSeasonNumber())
                .voteAverage(dto.getVoteAverage())
                .airDate(dto.getAirDate())
                .name(dto.getName())
                .posterPath(dto.getPosterPath())
                .overview(dto.getOverview())
                .build();
    }
    public static SeasonDto mapToDto(Season entity){
        return SeasonDto.builder()
                .airDate(entity.getAirDate())
                .seasonNumber(entity.getSeasonNumber())
                .voteAverage(entity.getVoteAverage())
                .name(entity.getName())
                .overview(entity.getOverview())
                .posterPath(entity.getPosterPath())
                .build();
    }

}
