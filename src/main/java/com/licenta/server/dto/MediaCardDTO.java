package com.licenta.server.dto;
import com.licenta.server.models.MediaType;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MediaCardDTO {
    private int tmdbId;
    private String title;
    private String posterPath;
    private MediaType mediaType;
}
