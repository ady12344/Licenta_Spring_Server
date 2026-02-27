package com.licenta.server.TMDBStuff;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TmdbRoleDTO {
    @JsonProperty("character")
    private String character;

    @JsonProperty("episode_count")
    private int episodeCount;
}
