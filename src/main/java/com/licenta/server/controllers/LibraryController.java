package com.licenta.server.controllers;

import com.licenta.server.dto.LibraryItemDTO;
import com.licenta.server.dto.MediaCardDTO;
import com.licenta.server.dto.PagedResponseDto;
import com.licenta.server.models.MediaType;
import com.licenta.server.services.UserLibraryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/library")
@RestController
@RequiredArgsConstructor
public class LibraryController {
    private final UserLibraryService userLibraryService;

    private String getUsername() {
        return (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    @PostMapping("/add")
    public ResponseEntity<String> addLibrary(@RequestBody MediaCardDTO library) {
        userLibraryService.addToLibrary(getUsername(), library);
        return ResponseEntity.ok("Added to library");
    }

    @DeleteMapping("/remove")
    public ResponseEntity<String> remove(@RequestParam MediaType mediaType,
                                         @RequestParam Integer tmdbId) {
        userLibraryService.removeFromLibrary(getUsername(), tmdbId, mediaType);
        return ResponseEntity.ok("Removed from library");
    }

    @GetMapping
    public ResponseEntity<PagedResponseDto<MediaCardDTO>> getAllLibrary(
            @RequestParam(required = false) MediaType type,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(
                userLibraryService.getLibrary(getUsername(), type, page - 1, size));
    }

    @GetMapping("/check")
    public ResponseEntity<Boolean> check(@RequestParam Integer tmdbId,
                                         @RequestParam MediaType mediaType) {
        return ResponseEntity.ok(
                userLibraryService.isInLibrary(getUsername(), tmdbId, mediaType));
    }
}
