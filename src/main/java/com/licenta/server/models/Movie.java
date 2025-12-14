package com.licenta.server.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "movie_data")
@Builder
public class Movie {
    @Id
    @GeneratedValue
    @JsonIgnore
    private Long id;
    private Integer tmdbId;
    private String title;
    @Column(columnDefinition = "TEXT")
    private String overview;
    private String releaseDate;
    private List<Integer> genres;
    private Double rating;
    private Integer voteCount;
    private String posterPath;
    private String backdropPath;
}
