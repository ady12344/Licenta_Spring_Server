package com.licenta.server.dto;

import com.licenta.server.models.ReviewSentiment;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewSummaryDTO {
    private long totalReviews;
    private long positiveReviews;
    private long negativeReviews;
    private double positivePercentage;
    private ReviewSentiment sentiment;
}
