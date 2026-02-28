package com.licenta.server.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public abstract class BaseCastDTO {
    @JsonProperty("name")
    protected String name;

    @JsonProperty("profile_path")
    protected String profilePath;

    @JsonProperty("order")
    protected int order;
}
