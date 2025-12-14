package com.licenta.server.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MovieDTO {

    private Integer tmdbId;
    private String title;
    private String overview;
    private String releaseDate;
    private List<Integer> genres;
    private Double rating;
    private String posterPath;
    private String backdropPath;
    private Integer voteCount;
}
