package com.licenta.server.controllers;

import com.licenta.server.TMDBStuff.TmdbMovies.TmdbMovieCardResults;
import com.licenta.server.dto.MovieDTO;
import com.licenta.server.models.Movie;
import com.licenta.server.services.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequiredArgsConstructor
@RequestMapping("/api/movies")
public class MovieController {
    private final MovieService movieService;
    @GetMapping("/find/movie/{id}")
    public ResponseEntity<MovieDTO> findMovie(@PathVariable Integer id) {
        return movieService.getMovieById(id);
    }

    @GetMapping("/search/{query}/{page}")
    public ResponseEntity<TmdbMovieCardResults> searchMovies(@PathVariable("page") int page , @PathVariable("query") String query){
        return movieService.searchMovieByTitle(page,query);
    }

    @GetMapping("/now_playing/{page}")
    public ResponseEntity<TmdbMovieCardResults> getNowPlayingMovies(@PathVariable int page) {
        return movieService.getNowPlayingMovies(page);
    }


    @GetMapping("/popular/{page}")
    public ResponseEntity<TmdbMovieCardResults> getPopularMovies(@PathVariable int page) {
        return movieService.getPopularMovies(page);
    }

    @GetMapping("/upcoming/{page}")
    public ResponseEntity<TmdbMovieCardResults> getUpcomingMovies(@PathVariable int page) {
        return movieService.getUpcomingMovies(page);
    }

    @PostMapping("/add/test")
    public void addMovie(@RequestParam Integer id){
         movieService.addMovie(id);
    }

}
