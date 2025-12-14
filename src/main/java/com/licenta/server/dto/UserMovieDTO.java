package com.licenta.server.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserMovieDTO {
    private Long userId;
    private Integer movieId;
    private String media_type;
}
