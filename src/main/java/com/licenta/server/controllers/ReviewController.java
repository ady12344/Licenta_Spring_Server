package com.licenta.server.controllers;

import com.licenta.server.dto.PagedResponseDto;
import com.licenta.server.dto.ReviewRequestDTO;
import com.licenta.server.dto.ReviewResponseDTO;
import com.licenta.server.dto.ReviewSummaryDTO;
import com.licenta.server.models.MediaType;
import com.licenta.server.services.ReviewService;
import lombok.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reviews")
public class ReviewController {
    private final ReviewService reviewService;

    private String getUsername() {
        return (String) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
    }

    @PostMapping
    public ResponseEntity<String> add(@RequestBody ReviewRequestDTO dto) {
        reviewService.addReview(getUsername(), dto);
        return ResponseEntity.ok("Review added!");
    }

    @DeleteMapping
    public ResponseEntity<String> delete(@RequestParam Integer tmdbId,
                                         @RequestParam MediaType mediaType) {
        reviewService.removeReview(getUsername(), tmdbId, mediaType);
        return ResponseEntity.ok("Review deleted!");
    }

    @GetMapping("/summary")
    public ResponseEntity<ReviewSummaryDTO> getSummary(
            @RequestParam Integer tmdbId,
            @RequestParam MediaType mediaType) {
        return ResponseEntity.ok(
                reviewService.getSummary(tmdbId, mediaType));
    }

    @GetMapping
    public ResponseEntity<PagedResponseDto<ReviewResponseDTO>> getReviews(
            @RequestParam Integer tmdbId,
            @RequestParam MediaType mediaType,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(
                reviewService.getReviews(tmdbId, mediaType, page - 1, size));
    }

    @GetMapping("/user")
    public ResponseEntity<ReviewResponseDTO> getUserReview(
            @RequestParam Integer tmdbId,
            @RequestParam MediaType mediaType) {
        return ResponseEntity.ok(
                reviewService.getUserReview(getUsername(), tmdbId, mediaType));
    }

    @PutMapping("/edit")
    public ResponseEntity<String> edit(@RequestBody ReviewRequestDTO dto) {
        reviewService.editReview(getUsername(), dto);
        return ResponseEntity.ok("Review edited!");
    }
}
