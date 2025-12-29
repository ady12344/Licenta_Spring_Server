package com.licenta.server.TMDBStuff;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import lombok.Getter;

import java.time.Instant;
import java.time.LocalDate;

@Getter
public class TmdbSeasonDto {
    @JsonProperty("season_number")
    private int seasonNumber;
    @JsonProperty("episode_count")
    private int episodeCount;
    private String name;
    private String overview;
    @JsonProperty("poster_path")
    private String posterPath;
    @JsonProperty("air_date")
    private LocalDate airDate;
    @JsonProperty("vote_average")
    private Double voteAverage;

}
