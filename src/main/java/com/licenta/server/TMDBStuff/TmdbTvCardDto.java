package com.licenta.server.TMDBStuff;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class TmdbTvCardDto {
    @JsonProperty("id")
    private Integer tmdbTvId;
    @JsonProperty("name")
    private String name;
    @JsonProperty("poster_path")
    private String posterPath;
}
