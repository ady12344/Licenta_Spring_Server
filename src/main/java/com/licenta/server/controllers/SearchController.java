package com.licenta.server.controllers;

import com.licenta.server.dto.MediaCardDTO;
import com.licenta.server.dto.PagedResponseDto;
import com.licenta.server.services.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
public class SearchController {
    private final SearchService searchService;

    // GET /api/search?query=batman&page=1&type=movie
    // GET /api/search?query=batman&page=1           (all types)
    @GetMapping
    public ResponseEntity<PagedResponseDto<MediaCardDTO>> search(
            @RequestParam String query,
            @RequestParam(required = false) String type,
            @RequestParam(defaultValue = "1") int page

    ) {
        return ResponseEntity.ok(searchService.search(query, type , page));
    }

    // GET /api/search/discover?page=1&type=movie&genres=28,35
    // GET /api/search/discover?page=1               (all types, no genre)
    @GetMapping("/discover")
    public ResponseEntity<PagedResponseDto<MediaCardDTO>> discover(
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String genres,
            @RequestParam(defaultValue = "1") int page
    ) {
        return ResponseEntity.ok(searchService.discover(type, genres, page));
    }
}