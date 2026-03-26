package com.licenta.server.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserStatsDTO {
    private String username;
    private long totalMovies;
    private long totalTvShows;
    private long totalReviews;
    private long positiveReviews;
    private long negativeReviews;
}