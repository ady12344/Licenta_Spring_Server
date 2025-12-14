package com.licenta.server.controllers;

import com.licenta.server.TMDBStuff.TmdbTV.TvShowSeasonsCard;
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
    public ResponseEntity<List<TvShowSeasonsCard>> getSeasons(@PathVariable Integer id){
        return tvShowsService.getAllSeasonsFromTvShow(id);
    }
    @GetMapping("/{id}")
    public ResponseEntity<TvDTO> getTvShowDetails(@PathVariable Integer id){
        return tvShowsService.getTvShowById(id);
    }
}
