package com.licenta.server.TMDBStuff;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public class TvCastDTO {
    @JsonProperty("name")
    private String name;

    @JsonProperty("profile_path")
    private String profilePath;

    @JsonProperty("total_episode_count")
    private int totalEpisodeCount;

    @JsonProperty("order")
    private int order;

  /*  // În seriale, un actor are o listă de roluri (ex: poate juca 2 personaje)
    @JsonProperty("roles")
    private List<TmdbRoleDTO> roles;*/
}
