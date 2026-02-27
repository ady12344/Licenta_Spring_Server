package com.licenta.server.controllers;

import com.licenta.server.TMDBStuff.TmdbTvDto;
import com.licenta.server.dto.PagedResponseDto;
import com.licenta.server.dto.SeasonCardDto;
import com.licenta.server.dto.TvCardDto;
import com.licenta.server.dto.TvDto;
import com.licenta.server.services.MediaService;
import com.licenta.server.services.TelevisionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/tv")
public class TvTestControllers {
    private final TelevisionService tvService;
    // TMDB raw details (if you want to expose it)
    // GET /tv/tmdb/{id}
    @GetMapping("/tmdb/{id}")
    public ResponseEntity<TmdbTvDto> fetchTvShowTmdb(@PathVariable int id) {
        return ResponseEntity.ok(tvService.fetchTvShowTmdb(id));
    }

    // Cached/DB-first + fallback to TMDB mapped to TvDto
    // GET /tv/{id}
    @GetMapping("/{id}")
    public ResponseEntity<TvDto> getTvDetails(@PathVariable int id) {
        return ResponseEntity.ok(tvService.getTvDetails(id));
    }

    // Search by title
    // GET /tv/search?query=...&page=1
    @GetMapping("/search")
    public ResponseEntity<PagedResponseDto<TvCardDto>> searchTvShowByTitle(
            @RequestParam String query,
            @RequestParam(defaultValue = "1") int page
    ) {
        return ResponseEntity.ok(tvService.searchTvShowByTitle(query, page));
    }

    // Popular
    // GET /tv/popular?page=1
    @GetMapping("/popular")
    public ResponseEntity<PagedResponseDto<TvCardDto>> getPopularTvShows(
            @RequestParam(defaultValue = "1") int page
    ) {
        return ResponseEntity.ok(tvService.getPopularTvShows(page));
    }

    // Top rated
    // GET /tv/top-rated?page=1
    @GetMapping("/top-rated")
    public ResponseEntity<PagedResponseDto<TvCardDto>> getTopRatedTvShows(
            @RequestParam(defaultValue = "1") int page
    ) {
        return ResponseEntity.ok(tvService.getTopRatedTvShows(page));
    }

    // On the air
    // GET /tv/on-the-air?page=1
    @GetMapping("/on-the-air")
    public ResponseEntity<PagedResponseDto<TvCardDto>> onTheAirTvShows(
            @RequestParam(defaultValue = "1") int page
    ) {
        return ResponseEntity.ok(tvService.onTheAirTvShows(page));
    }

}
