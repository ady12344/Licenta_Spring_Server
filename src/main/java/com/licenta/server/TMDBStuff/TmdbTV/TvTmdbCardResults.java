package com.licenta.server.TMDBStuff.TmdbTV;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TvTmdbCardResults {
    @JsonProperty("page")
    private int page;
    @JsonProperty("results")
    private List<TmdbTvCard> results;
    @JsonProperty("total_pages")
    private int totalPages;
}
