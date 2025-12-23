package com.licenta.server.dto;

import com.licenta.server.models.MediaType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
@Builder
public class MovieDto {
    private int tmdbId;
    private MediaType mediaType;
    private String title;
    private String overview;
    private String posterPath;
    private String backdropPath;
    private String releaseDate;      // string in API, LocalDate in DB if you want
    private Double tmdbRating;
    private Double popularity;
    private List<String> genres;
}
