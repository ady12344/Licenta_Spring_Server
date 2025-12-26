package com.licenta.server.controllers;

import com.licenta.server.TMDBStuff.TmdbClient;
import com.licenta.server.dto.MovieCardDto;
import com.licenta.server.dto.MovieDto;
import com.licenta.server.dto.PagedResponseDto;
import com.licenta.server.models.Media;
import com.licenta.server.models.MediaType;
import com.licenta.server.services.MediaService;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class TestController {
    private final TmdbClient tmdbClient;
    private final MediaService mediaService;

    // GET /api/test/movies/{tmdbId}
    @GetMapping("/{tmdbId}")
    public ResponseEntity<MovieDto> getMovieDetails(@PathVariable int tmdbId) {
        return ResponseEntity.ok(mediaService.getMovieDetails(tmdbId));
    }

    // POST /api/test/movies/{tmdbId}/sync
    @PostMapping("/{tmdbId}/sync")
    public Media syncMovie(@PathVariable int tmdbId) {
        return mediaService.upsertAndSyncMedia(MediaType.MOVIE, tmdbId);
    }

    // GET /api/test/movies/search?page=1&query=batman
    @GetMapping("/search")
    public ResponseEntity<PagedResponseDto<MovieCardDto>> search(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam String query
    ) {
        return ResponseEntity.ok(mediaService.searchMovieByTitle(page, query));
    }

    // GET /api/test/movies/popular?page=1
    @GetMapping("/popular")
    public ResponseEntity<PagedResponseDto<MovieCardDto>> popular(
            @RequestParam(defaultValue = "1") int page
    ) {
        return ResponseEntity.ok(mediaService.getPopularMovies(page));
    }

    // GET /api/test/movies/now-playing?page=1
    @GetMapping("/now-playing")
    public ResponseEntity<PagedResponseDto<MovieCardDto>> nowPlaying(
            @RequestParam(defaultValue = "1") int page
    ) {
        return ResponseEntity.ok(mediaService.getNowPlayingMovies(page));
    }

    // GET /api/test/movies/upcoming?page=1
    @GetMapping("/upcoming")
    public ResponseEntity<PagedResponseDto<MovieCardDto>> upcoming(
            @RequestParam(defaultValue = "1") int page
    ) {
        return ResponseEntity.ok(mediaService.getUpcomingMovies(page));
    }

    @PostMapping("/add")
    public ResponseEntity<?> add(@RequestParam String mediaType , int id ){
        mediaService.upsertAndSyncMedia(MediaType.valueOf(mediaType.toUpperCase()) , id);
        return ResponseEntity.ok().build();
    }
}
