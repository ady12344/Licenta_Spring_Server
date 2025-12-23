package com.licenta.server.TMDBStuff;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class TmdbSeasonDto {

    @JsonProperty("season_number")
    private int seasonNumber;
    @JsonProperty("name")
    private String name;
    @JsonProperty("overview")
    private String overview;
    @JsonProperty("episode_count")
    private int episodeCount;
    @JsonProperty("air_date")
    private String airDate;
    @JsonProperty("poster_path")
    private String posterPath;
}
