package com.licenta.server.TMDBStuff;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class TmdbMovieDto {

    private int id;

    private String title;

    private String overview;

    @JsonProperty("poster_path")
    private String posterPath;

    @JsonProperty("backdrop_path")
    private String backdropPath;

    @JsonProperty("release_date")
    private String releaseDate; // keep as String

    private Integer runtime;

    @JsonProperty("vote_average")
    private Double voteAverage;

    private Double popularity;
    private String status;
    private List<TmdbGenreDto> genres;
}
