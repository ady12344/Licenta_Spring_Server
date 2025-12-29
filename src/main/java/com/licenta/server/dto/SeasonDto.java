package com.licenta.server.dto;

import jakarta.persistence.Column;
import lombok.*;

import java.time.LocalDate;

@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SeasonDto {
    private int seasonNumber;
    private String name;
    private String overview;
    private String posterPath;
    private LocalDate airDate;
    private Double voteAverage;
}
