package com.licenta.server.controllers;

import com.licenta.server.TMDBStuff.CastDTO;
import com.licenta.server.TMDBStuff.TmdbClient;
import com.licenta.server.TMDBStuff.TmdbMovieDto;
import com.licenta.server.dto.MovieCardDto;
import com.licenta.server.dto.MovieDto;
import com.licenta.server.dto.PagedResponseDto;
import com.licenta.server.services.MovieService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
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

    @GetMapping("/testTMDBMovie")
    public ResponseEntity<TmdbMovieDto> testTMDBMovie(@RequestParam  int id){
        return ResponseEntity.ok(mediaService.test(id));
    }

}
