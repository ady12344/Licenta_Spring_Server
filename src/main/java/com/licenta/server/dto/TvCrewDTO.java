package com.licenta.server.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.licenta.server.TMDBStuff.TmdbTvCrewJobDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public class TvCrewDTO extends BaseCrewDTO {
    @JsonProperty("total_episode_count")
    private int totalEpisodeCount;

    @JsonProperty("jobs")
    private List<TmdbTvCrewJobDTO> jobs;

}
