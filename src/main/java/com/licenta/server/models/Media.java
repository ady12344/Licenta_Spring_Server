package com.licenta.server.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(
        uniqueConstraints = @UniqueConstraint(columnNames = {"tmdbId", "mediaType"})
)
public class Media {

    @Id
    @GeneratedValue
    private Long id;

    private int tmdbId;

    @Enumerated(EnumType.STRING)
    private MediaType mediaType; // MOVIE, TV

    private String title;
    @Column(columnDefinition = "TEXT")
    private String overview;
    private String posterPath;
    private String backdropPath;
    private LocalDate releaseDate;    // movie
    private LocalDate firstAirDate;   // tv
    @ElementCollection
    @CollectionTable(name = "media_genres", joinColumns = @JoinColumn(name = "media_id"))
    @Column(name = "genre")
    private List<String> genres;
    private Double voteAverage;
    private Double popularity;
    private Instant lastSyncedAt;
}
