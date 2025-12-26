package com.licenta.server.dto;

import com.licenta.server.models.MediaType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TvCardDto {
    private int tmdbTvId;
    private String name;
    private String posterPath;
    @Builder.Default
    private MediaType mediaType = MediaType.TV;
}
