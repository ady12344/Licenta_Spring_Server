package com.licenta.server.dto;

import com.licenta.server.models.MediaType;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewResponseDTO {
    private String username;
    private Boolean liked;
    private String content;
    private LocalDateTime createdAt;
}
