package com.licenta.server.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "seasons")
public class Seasons {
    @Id
    @GeneratedValue
    private Long id;
    private Integer seasonNumber;
    private String name;
    @Column(columnDefinition = "TEXT")
    private String overview;
    private String posterPath;
    private LocalDate airDate;
    private Integer episodeCount;
    @ManyToOne
    @JoinColumn(name = "tv_show_id")
    private TvShows tvShows;


}
