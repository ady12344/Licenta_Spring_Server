package com.licenta.server.TMDBStuff.TmdbMovies;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class TmdbMovieCardResults {
    @JsonProperty("page")
    private Integer page;
    @JsonProperty("results")
    private List<TmdbLightSearchResult> results;
    @JsonProperty("total_pages")
    private Integer totalPages;


}
