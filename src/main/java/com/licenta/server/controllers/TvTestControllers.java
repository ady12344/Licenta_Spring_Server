package com.licenta.server.controllers;

import com.licenta.server.TMDBStuff.TmdbTvDto;
import com.licenta.server.dto.*;
import com.licenta.server.services.TelevisionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/tv")
public class TvTestControllers {
    private final TelevisionService tvService;
    // TMDB raw details (if you want to expose it)
    // GET /tv/tmdb/{id}
    @GetMapping("/tmdb/{id}")
    public ResponseEntity<TmdbTvDto> fetchTvShowTmdb(@PathVariable int id) {
        return ResponseEntity.ok(tvService.fetchTvShowTmdb(id));
    }
    @GetMapping("/getSimilarShows")
    //Im gonna kms
    public ResponseEntity<PagedResponseDto<MediaCardDTO>> seeSimilarShows(@RequestParam int id, @RequestParam int page){
        return ResponseEntity.ok(tvService.seeSimilarShows(id,page));
    }
    @GetMapping("/getRecommendedShows")
    public ResponseEntity<PagedResponseDto<MediaCardDTO>> seeRecommendedShows(@RequestParam int id, @RequestParam int page){
        return ResponseEntity.ok(tvService.seeRecommendedShows(id,page));
    }
    //nope im fine
    @GetMapping("/getCastTV")
    public ResponseEntity<PagedResponseDto<TvCastDTO>> getCastTV(@RequestParam int id , @RequestParam int page, @RequestParam int size) {
        return ResponseEntity.ok(tvService.getTvShowCastPaged(id, page,size));
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
    public ResponseEntity<PagedResponseDto<MediaCardDTO>> searchTvShowByTitle(
            @RequestParam String query,
            @RequestParam(defaultValue = "1") int page
    ) {
        return ResponseEntity.ok(tvService.searchTvShowByTitle(query, page));
    }

    // Popular
    // GET /tv/popular?page=1
    @GetMapping("/popular")
    public ResponseEntity<PagedResponseDto<MediaCardDTO>> getPopularTvShows(
            @RequestParam(defaultValue = "1") int page
    ) {
        return ResponseEntity.ok(tvService.getPopularTvShows(page));
    }

    // Top rated
    // GET /tv/top-rated?page=1
    @GetMapping("/top-rated")
    public ResponseEntity<PagedResponseDto<MediaCardDTO>> getTopRatedTvShows(
            @RequestParam(defaultValue = "1") int page
    ) {
        return ResponseEntity.ok(tvService.getTopRatedTvShows(page));
    }

    // On the air
    // GET /tv/on-the-air?page=1
    @GetMapping("/on-the-air")
    public ResponseEntity<PagedResponseDto<MediaCardDTO>> onTheAirTvShows(
            @RequestParam(defaultValue = "1") int page
    ) {
        return ResponseEntity.ok(tvService.onTheAirTvShows(page));
    }
    @GetMapping("/discover")
    public ResponseEntity<PagedResponseDto<MediaCardDTO>> discoverTv(
            @RequestParam(required = false) String genres,
            @RequestParam(defaultValue = "1") int page
    ) {
        return ResponseEntity.ok(tvService.discoverTv(genres, page));
    }

}
