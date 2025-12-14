package com.licenta.server.controllers;

import com.licenta.server.TMDBStuff.TmdbMovies.TmdbCardResults;
import com.licenta.server.dto.MovieDTO;
import com.licenta.server.dto.UserMovieDTO;
import com.licenta.server.models.Movie;
import com.licenta.server.services.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequiredArgsConstructor
@RequestMapping("/api/movies")
public class MovieController {
    private final MovieService movieService;
    @GetMapping("/find/movie/{id}")
    public ResponseEntity<Movie> findMovie(@PathVariable Integer id) {
        return movieService.getMovieById(id);
    }

    @GetMapping("/search/{query}/{page}")
    public ResponseEntity<TmdbCardResults> searchMovies(@PathVariable("page") int page , @PathVariable("query") String query){
        return movieService.searchMovieByTitle(page,query);
    }

    @GetMapping("/now_playing/{page}")
    public ResponseEntity<TmdbCardResults> getNowPlayingMovies(@PathVariable int page) {
        return movieService.getNowPlayingMovies(page);
    }


    @GetMapping("/popular/{page}")
    public ResponseEntity<TmdbCardResults> getPopularMovies(@PathVariable int page) {
        return movieService.getPopularMovies(page);
    }

    @GetMapping("/upcoming/{page}")
    public ResponseEntity<TmdbCardResults> getUpcomingMovies(@PathVariable int page) {
        return movieService.getUpcomingMovies(page);
    }

    @PostMapping("/add/test")
    public void addMovie(@RequestBody MovieDTO dto){
         movieService.addMovie(dto);
    }


}
