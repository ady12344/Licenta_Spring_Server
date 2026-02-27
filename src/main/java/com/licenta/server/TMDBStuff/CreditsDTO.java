package com.licenta.server.TMDBStuff;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter @Getter
@NoArgsConstructor
public class CreditsDTO {
    private List<CastDTO> cast;
    private List<CrewDTO> crew;
}
