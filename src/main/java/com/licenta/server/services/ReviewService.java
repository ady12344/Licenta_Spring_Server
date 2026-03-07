package com.licenta.server.services;

import com.licenta.server.dto.PagedResponseDto;
import com.licenta.server.dto.ReviewRequestDTO;
import com.licenta.server.dto.ReviewResponseDTO;
import com.licenta.server.dto.ReviewSummaryDTO;
import com.licenta.server.exception.ReviewAlreadyExists;
import com.licenta.server.exception.ReviewNotFound;
import com.licenta.server.models.MediaType;
import com.licenta.server.models.Review;
import com.licenta.server.models.ReviewSentiment;
import com.licenta.server.models.User;
import com.licenta.server.repository.ReviewRepository;
import com.licenta.server.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;

    @Transactional
    public void addReview(String username, ReviewRequestDTO reviewRequestDTO) {
        User user = userRepository.findByUsername(username).orElseThrow(() ->  new RuntimeException("User not found!"));
        if(reviewRepository.existsByUserIdAndTmdbIdAndMediaType(user.getId() , reviewRequestDTO.getTmdbId(), reviewRequestDTO.getMediaType())){
            throw new ReviewAlreadyExists("You have already reviewed this title!");
        }
        Review review = Review.builder()
                .user(user)
                .content(reviewRequestDTO.getContent())
                .mediaType(reviewRequestDTO.getMediaType())
                .liked(reviewRequestDTO.getLiked())
                .createdAt(LocalDateTime.now())
                .tmdbId(reviewRequestDTO.getTmdbId())
                .build();
        reviewRepository.save(review);
    }

    @Transactional
    public void removeReview(String username, Integer tmdbId , MediaType mediaType) {
        User user = userRepository.findByUsername(username).orElseThrow(()->new RuntimeException("User not found!"));
        Review review = reviewRepository.findByUserIdAndTmdbIdAndMediaType(user.getId() , tmdbId , mediaType).orElseThrow(()->new ReviewNotFound("Review not found!"));
        reviewRepository.delete(review);
    }

    public ReviewSummaryDTO getSummary(Integer tmdbId, MediaType mediaType) {
        long total    = reviewRepository.countByTmdbIdAndMediaType(tmdbId, mediaType);
        long positive = reviewRepository.countByTmdbIdAndMediaTypeAndLiked(tmdbId, mediaType, true);
        long negative = total - positive;
        double percentage = total > 0 ? (double) positive / total * 100 : 0;

        return ReviewSummaryDTO.builder()
                .totalReviews(total)
                .positiveReviews(positive)
                .negativeReviews(negative)
                .positivePercentage(Math.round(percentage * 10.0) / 10.0)
                .sentiment(ReviewSentiment.fromScore(positive, total))
                .build();
    }
    public PagedResponseDto<ReviewResponseDTO> getReviews(Integer tmdbId, MediaType mediaType, int page, int size) {
        Page<Review> result = reviewRepository
                .findByTmdbIdAndMediaTypeOrderByCreatedAtDesc(tmdbId, mediaType, PageRequest.of(page, size));

        List<ReviewResponseDTO> reviews = result.getContent().stream()
                .map(r -> ReviewResponseDTO.builder()
                        .username(r.getUser().getUsername())
                        .liked(r.getLiked())
                        .content(r.getContent())
                        .createdAt(r.getCreatedAt())
                        .build())
                .toList();

        return PagedResponseDto.<ReviewResponseDTO>builder()
                .page(result.getNumber() + 1)
                .results(reviews)
                .totalPages(result.getTotalPages())
                .build();
    }

    public Boolean getUserReview(String username, Integer tmdbId, MediaType mediaType) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found!"));

        return reviewRepository
                .findByUserIdAndTmdbIdAndMediaType(user.getId(), tmdbId, mediaType)
                .map(Review::getLiked)
                .orElse(null); // null means user hasn't reviewed yet
    }

}
