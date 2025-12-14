package com.licenta.server.TMDBStuff.TmdbMovies;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TmdbCastDTO{
    @JsonProperty("name")
    private String name;
    @JsonProperty("character")
    private String character;
    @JsonProperty("profile_path")
    private String profilePath;
}
