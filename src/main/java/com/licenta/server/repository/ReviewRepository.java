package com.licenta.server.repository;

import com.licenta.server.models.MediaType;
import com.licenta.server.models.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    boolean existsByUserIdAndTmdbIdAndMediaType(Long userId, Integer tmdb, MediaType mediaType);
    Page<Review> findByTmdbIdAndMediaTypeOrderByCreatedAtDesc(Integer tmdbId, MediaType mediaType, Pageable pageable);
    long countByTmdbIdAndMediaType(Integer tmdbId, MediaType mediaType);
    long countByTmdbIdAndMediaTypeAndLiked(Integer tmdbId, MediaType mediaType, Boolean liked);
    Optional<Review> findByUserIdAndTmdbIdAndMediaType(Long userId, Integer tmdbId, MediaType mediaType);
    long countByUserId(Long userId);
    long countByUserIdAndLiked(Long userId, Boolean liked);
}
