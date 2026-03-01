package com.licenta.server.dto;

import com.licenta.server.models.MediaType;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovieCardDto {
    private int tmdbId;
    private String title;
    private String posterPath;
}
