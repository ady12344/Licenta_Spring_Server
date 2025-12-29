package com.licenta.server.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter @Getter
@Builder
@AllArgsConstructor
public class SeasonCardDto {
    private String name;
    private String posterPath;
    private int seasonNumber;
}
