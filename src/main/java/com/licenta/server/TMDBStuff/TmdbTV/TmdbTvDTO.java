package com.licenta.server.TMDBStuff.TmdbTV;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.licenta.server.TMDBStuff.TmdbGenreDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TmdbTvDTO {
    @JsonProperty("id")
    private Integer id;
    @JsonProperty("first_air_date")
    private String airDate;
    @JsonProperty("name")
    private String name;
    @JsonProperty("overview")
    private String overview;
    @JsonProperty("genres")
    private List<TmdbGenreDTO> genres;
    @JsonProperty("number_of_seasons")
    private Integer numberOfSeasons;
    @JsonProperty("poster_path")
    private String posterPath;
    @JsonProperty("backdrop_path")
    private String backdropPath;
    @JsonProperty("vote_count")
    private Integer voteCount;
    @JsonProperty("vote_average")
    private Double voteAverage;
    @JsonProperty("status")
    private String status;
    @JsonProperty("seasons")
    private List<TvTmdbSeasonCard> seasons;
}
