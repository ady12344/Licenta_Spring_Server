package com.licenta.server.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter @Getter
@NoArgsConstructor
public class CreditsDTO<T extends BaseCastDTO, U extends BaseCrewDTO> {
    private List<T> cast;
    private List<U> crew;
}
