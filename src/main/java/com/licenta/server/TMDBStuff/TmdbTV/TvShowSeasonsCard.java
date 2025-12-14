package com.licenta.server.TMDBStuff.TmdbTV;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class TvShowSeasonsCard {
    @JsonProperty("season_number")
    private Integer seasonNumber;
    @JsonProperty("name")
    private String name;
    @JsonProperty("poster_path")
    private String posterPath;
}
