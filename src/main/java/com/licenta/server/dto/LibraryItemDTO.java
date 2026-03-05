package com.licenta.server.dto;


import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LibraryItemDTO {
    private Integer tmdbId;
    private String mediaType;
    private String title;
    private String posterPath;
    private LocalDateTime createdAt;
}
