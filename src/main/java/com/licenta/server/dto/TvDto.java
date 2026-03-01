package com.licenta.server.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
@Builder
public class TvDto {
    private int tmdbId;
    private String title;
    private String overview;
    private String posterPath;
    private String backdropPath;
    private String firstAirDate; // keep as String
    private String status;
    private Integer numberOfSeasons;
    private Double tmdbRating;
    private List<String> genres;
    private String directorName;
    private List<TvCastDTO> topCast;
    private List<TvCardDto> similarShows;
    private List<TvCardDto> recommendations;
}
