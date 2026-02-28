package com.licenta.server.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class BaseCrewDTO {
    @JsonProperty("name")
    protected String name;

    @JsonProperty("profile_path")
    protected String profilePath;

    @JsonProperty("department")
    protected String department;
}
