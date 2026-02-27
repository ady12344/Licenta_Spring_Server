package com.licenta.server.dto;

import com.licenta.server.models.MediaType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MovieCardDto {
    private int tmdbId;
    private String title;
    private String posterPath;
}
