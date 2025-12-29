package com.licenta.server.models;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDate;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
/*@Table(
        name = "seasons",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_season_media_number",
                        columnNames = {"media_id", "season_number"}
                )
        },
        indexes = {
                @Index(name = "idx_season_media", columnList = "media_id")
        }
)*/ //Ill see if it works
@Table(name = "seasons")
public class Season {
    @Id
    @GeneratedValue
    private Long Id;
    private int seriesId;
    private int seasonNumber;
    private String name;
    @Column(columnDefinition = "TEXT")
    private String overview;
    private String posterPath;
    private LocalDate airDate;
    private Double voteAverage;
    private Instant lastSyncedAt;


}
