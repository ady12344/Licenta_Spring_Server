package com.licenta.server.TMDBStuff;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;

@Getter
public class TmdbMovieCardDto {
    @JsonProperty("id")
    private Integer tmdbId;
    @JsonProperty("title")
    private String title;
    @JsonProperty("poster_path")
    private String posterPath;
}
