package com.licenta.server.controllers;

import com.licenta.server.TMDBStuff.TmdbTV.TvTmdbCardResults;
import com.licenta.server.TMDBStuff.TmdbTV.TvTmdbSeasonCard;
import com.licenta.server.dto.TvDTO;
import com.licenta.server.services.TvShowsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/tv")
@RequiredArgsConstructor
public class TvShowsController {
    private final TvShowsService tvShowsService;
    @GetMapping("/{id}/seasons")
    public ResponseEntity<List<TvTmdbSeasonCard>> getSeasons(@PathVariable Integer id){
        return ResponseEntity.ok(tvShowsService.getAllSeasonsFromTvShow(id));
    }
    @GetMapping("/{id}")
    public ResponseEntity<TvDTO> getTvShowDetails(@PathVariable Integer id){
        return ResponseEntity.ok(tvShowsService.getTvShowDetails(id));
    }
    @GetMapping("/search/{query}/{page}")
    public ResponseEntity<TvTmdbCardResults> search(@PathVariable String query , @PathVariable int page){
        return ResponseEntity.ok(tvShowsService.searchResults(query, page));
    }
    @GetMapping("/popular/{page}")
    public ResponseEntity<TvTmdbCardResults> popular(@PathVariable int page){
        return ResponseEntity.ok(tvShowsService.popular(page));
    }
    @GetMapping("/top_rated/{page}")
    public ResponseEntity<TvTmdbCardResults> topRated(@PathVariable int page){
        return ResponseEntity.ok(tvShowsService.topRated(page));
    }
    @GetMapping("/on_the_air/{page}")
    public ResponseEntity<TvTmdbCardResults> onTheAir( @PathVariable int page){
        return ResponseEntity.ok(tvShowsService.onTheAir(page));
    }
}
