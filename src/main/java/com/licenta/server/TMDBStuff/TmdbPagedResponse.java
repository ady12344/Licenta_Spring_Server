package com.licenta.server.TMDBStuff;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;

@Getter
public class TmdbPagedResponse<T>{
    @JsonProperty("page")
    private Integer page;
    @JsonProperty("results")
    private List<T> results;
    @JsonProperty("total_pages")
    private Integer totalPages;
}
