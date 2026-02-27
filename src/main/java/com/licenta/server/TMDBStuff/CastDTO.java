package com.licenta.server.TMDBStuff;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class CastDTO {
    @JsonProperty("name")
    private String name;
    @JsonProperty("character")
    private String character;
    @JsonProperty("profile_path")
    private String profilePath; // URL for the actor's photo
    @JsonProperty("order")
    private int order;
}
