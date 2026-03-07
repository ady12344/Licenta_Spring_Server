package com.licenta.server.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TvCardDto {
    private int tmdbTvId;
    private String title;
    private String posterPath;
}
