package com.licenta.server.TMDBStuff.TmdbMovies;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.licenta.server.TMDBStuff.TmdbGenreDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TmdbMovieDTO {
    @JsonProperty("id")
    private Integer tmdbId;
    @JsonProperty("title")
    private String title;
    @JsonProperty("overview")
    private String overview;
    @JsonProperty("release_date")
    private String releaseDate;
    @JsonProperty("genres")
    private List<TmdbGenreDTO> genres;
    @JsonProperty("vote_average")
    private Double rating;
    @JsonProperty("vote_count")
    private Integer voteCount;
    @JsonProperty("poster_path")
    private String posterPath;
    @JsonProperty("backdrop_path")
    private String backdropPath;
}
