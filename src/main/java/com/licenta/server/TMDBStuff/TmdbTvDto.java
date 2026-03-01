package com.licenta.server.TMDBStuff;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.licenta.server.dto.CreditsDTO;
import com.licenta.server.dto.TvCastDTO;
import com.licenta.server.dto.TvCrewDTO;
import lombok.Data;

import java.util.List;

@Data
public class TmdbTvDto {

    private int id;

    private String name;

    @JsonProperty("original_name")
    private String originalName;

    private String overview;

    @JsonProperty("poster_path")
    private String posterPath;

    @JsonProperty("backdrop_path")
    private String backdropPath;

    @JsonProperty("first_air_date")
    private String firstAirDate; // keep as String

    @JsonProperty("number_of_seasons")
    private Integer numberOfSeasons;

    @JsonProperty("vote_average")
    private Double voteAverage;

    private String status;

    private List<TmdbGenreDto> genres;
    @JsonProperty("aggregate_credits")
    private CreditsDTO<TvCastDTO, TvCrewDTO> credits;
    @JsonProperty("similar")
    private TmdbPagedResponse<TmdbTvCardDto> similar;
    @JsonProperty("recommendations")
    private TmdbPagedResponse<TmdbTvCardDto> recommendations;
}
