package com.licenta.server.controllers;

import com.licenta.server.dto.*;
import com.licenta.server.TMDBStuff.TmdbClient;
import com.licenta.server.TMDBStuff.TmdbMovieDto;
import com.licenta.server.services.MovieService;
import lombok.AllArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/movie")
public class MovieTestController {
    private final TmdbClient tmdbClient;
    private final MovieService mediaService;

    @GetMapping("/getCast")
    public ResponseEntity<PagedResponseDto<CastDTO>> getMovieCastAndCrew(@RequestParam int id,
                                                                         @RequestParam(defaultValue = "0") int page,
                                                                         @RequestParam(defaultValue = "15") int size){

        PagedResponseDto<CastDTO> response = mediaService.getMovieCastPaged(id, page, size);

        return ResponseEntity.ok(response);

    }
    // GET /api/test/movies/{tmdbId}
    @GetMapping("/{tmdbId}")
    public ResponseEntity<MovieDto> getMovieDetails(@PathVariable int tmdbId) {
        return ResponseEntity.ok(mediaService.getMovieDetails(tmdbId));
    }

    // GET /api/test/movies/search?page=1&query=batman
    @GetMapping("/search")
    public ResponseEntity<PagedResponseDto<MediaCardDTO>> search(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam String query
    ) {
        return ResponseEntity.ok(mediaService.searchMovieByTitle(page, query));
    }

    // GET /api/test/movies/popular?page=1
    @GetMapping("/popular")
    public ResponseEntity<PagedResponseDto<MediaCardDTO>> popular(
            @RequestParam(defaultValue = "1") int page
    ) {
        return ResponseEntity.ok(mediaService.getPopularMovies(page));
    }

    @GetMapping("/getSimilarMovies")
    public ResponseEntity<PagedResponseDto<MediaCardDTO>> getSimilarMovies(@RequestParam  int id ,@RequestParam int page){
        return ResponseEntity.ok(mediaService.seeAllSimilarMovies(id,page));
    }
    @GetMapping("/getRecommendedMovies")
    public ResponseEntity<PagedResponseDto<MediaCardDTO>> getRecommendedMovies(@RequestParam  int id ,@RequestParam int page){
        return ResponseEntity.ok(mediaService.seeRecommendedMovies(id,page));
    }

    // GET /api/test/movies/now-playing?page=1
    @GetMapping("/now-playing")
    public ResponseEntity<PagedResponseDto<MediaCardDTO>> nowPlaying(
            @RequestParam(defaultValue = "1") int page
    ) {
        return ResponseEntity.ok(mediaService.getNowPlayingMovies(page));
    }

    // GET /api/test/movies/upcoming?page=1
    @GetMapping("/upcoming")
    public ResponseEntity<PagedResponseDto<MediaCardDTO>> upcoming(
            @RequestParam(defaultValue = "1") int page
    ) {
        return ResponseEntity.ok(mediaService.getUpcomingMovies(page));
    }

    @GetMapping("/testTMDBMovie")
    public ResponseEntity<TmdbMovieDto> testTMDBMovie(@RequestParam  int id){
        return ResponseEntity.ok(mediaService.test(id));
    }
    @GetMapping("/discover")
    public ResponseEntity<PagedResponseDto<MediaCardDTO>> discoverMovies(
            @RequestParam(required = false) String genres,
            @RequestParam(defaultValue = "1") int page
    ) {
        return ResponseEntity.ok(mediaService.discoverMovies(genres, page));
    }


}
