package com.licenta.server.TMDBStuff.TmdbMovies;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class TmdbGenreDTO {
    @JsonProperty("id")
    private Integer id;
    @JsonProperty("name")
    private String name; //Idk if i will need this or ill just keep the genres ids and map them in the frontend
}
