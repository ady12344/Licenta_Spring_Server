package com.licenta.server.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.licenta.server.TMDBStuff.TmdbRoleDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public class TvCastDTO extends BaseCastDTO{
    @JsonProperty("total_episode_count")
    private int totalEpisodeCount;

    @JsonProperty("roles")
    private List<TmdbRoleDTO> roles;
}