package com.licenta.server.dto;


import lombok.*;

import java.util.List;
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TvDTO {
    private Integer tmdb_id;
    private String name;
    private String overview;
    private String release_date;
    private List<Integer> genres;
    private Integer numberOfSeasons;
    private String posterPath;
    private String backdropPath;
    private Integer voteCount;
    private Double voteAverage;
    private String status;
}
