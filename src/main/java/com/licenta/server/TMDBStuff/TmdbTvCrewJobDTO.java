package com.licenta.server.TMDBStuff;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class TmdbTvCrewJobDTO {
    @JsonProperty("job")
    private String job;

    @JsonProperty("episode_count")
    private int episodeCount;
}
