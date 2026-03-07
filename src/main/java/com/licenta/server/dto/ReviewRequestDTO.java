package com.licenta.server.dto;

import com.licenta.server.models.MediaType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReviewRequestDTO {
    private Integer tmdbId;
    private MediaType mediaType;
    private Boolean liked;
    private String content;
}
