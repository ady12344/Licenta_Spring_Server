package com.licenta.server.TMDBStuff.TmdbMovies;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

//For card result search
@Data
public class TmdbLightSearchResult {
    @JsonProperty("id")
    private Integer tmdbId;
    @JsonProperty("title")
    private String title;
    @JsonProperty("poster_path")
    private String posterPath;
}
