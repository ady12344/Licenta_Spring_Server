package com.licenta.server.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "tv_shows")
public class TvShows {
    @Id
    private Integer tmdb_id;
    private String name;
    @Column(columnDefinition = "TEXT")
    private String overview;
    private String releaseDate;
    @ElementCollection
    private List<Integer> genres;
    private Integer numberOfSeasons;
    private String posterPath;
    private String backdropPath;
    private Integer voteCount;
    private Double voteAverage;
    private String status;

}
