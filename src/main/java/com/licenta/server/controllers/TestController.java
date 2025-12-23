package com.licenta.server.controllers;

import com.licenta.server.TMDBStuff.TmdbClient;
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

    @GetMapping("/search")
    public Object test(@RequestParam String q) {
        return tmdbClient.searchTvByTitle(q, 1);
    }
    @GetMapping("/getDetails")
    public Object details(@RequestParam int q) {
        return tmdbClient.getTvShowDetails(q);
    }
    @GetMapping("/popular")
    public Object popular(@RequestParam int q) {
        return tmdbClient.popularTvShows(q);
    }
    @GetMapping("/playingnow")
    public Object toprated(@RequestParam int q) {
        return tmdbClient.topRatedTvShow(q);
    }
    @GetMapping("/upcoming")
    public Object ontheair(@RequestParam int q) {
        return tmdbClient.onTheAir(q);
    }

    @PostMapping("/add")
    public ResponseEntity<?> add(@RequestParam String mediaType , int id ){
        mediaService.upsertAndSyncMedia(MediaType.valueOf(mediaType.toUpperCase()) , id);
        return ResponseEntity.ok().build();
    }
}
