package com.licenta.server.TMDBStuff;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;

@Getter
public class TmdbMultiCardDTO{
    @JsonProperty("id")
    private Integer id;

    @JsonProperty("title")
    private String title;       // movies use "title"

    @JsonProperty("name")
    private String name;        // tv shows use "name"

    @JsonProperty("poster_path")
    private String posterPath;

    @JsonProperty("media_type")
    private String mediaType;   // "movie" or "tv"
}