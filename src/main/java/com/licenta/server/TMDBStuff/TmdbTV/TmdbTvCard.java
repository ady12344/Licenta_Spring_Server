package com.licenta.server.TMDBStuff.TmdbTV;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TmdbTvCard {
    @JsonProperty("id")
    private Integer tmdbTvId;
    @JsonProperty("name")
    private String name;
    @JsonProperty("poster_path")
    private String posterPath;
}
