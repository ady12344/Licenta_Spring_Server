package com.licenta.server.TMDBStuff;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class TmdbSeasonCardDto {

    @JsonProperty("season_number")
    private int seasonNumber;
    @JsonProperty("name")
    private String name;
    @JsonProperty("poster_path")
    private String posterPath;
}
