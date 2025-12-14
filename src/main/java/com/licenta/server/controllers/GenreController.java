package com.licenta.server.controllers;

import com.licenta.server.models.Genre;
import com.licenta.server.repository.GenreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/genres")
@RequiredArgsConstructor
public class GenreController {
    private final GenreRepository genreRepository;
    @GetMapping("/getAllGenres")
    public List<Genre> getAllGenres(){
        return genreRepository.findAll();
    }

}
