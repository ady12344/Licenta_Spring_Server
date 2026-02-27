package com.licenta.server.TMDBStuff;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public class TvCrewDTO {

    @JsonProperty("name")
    private String name;

    @JsonProperty("profile_path")
    private String profilePath;

    @JsonProperty("department")
    private String department;

    @JsonProperty("total_episode_count")
    private int totalEpisodeCount;

    // La seriale, echipa are o listă de job-uri (ex: Director în 5 ep, Producer în 10 ep)
    @JsonProperty("jobs")
    private List<TmdbTvCrewJobDTO> jobs;

}
