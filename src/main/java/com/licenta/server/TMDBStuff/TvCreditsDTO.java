package com.licenta.server.TMDBStuff;

import com.licenta.server.dto.TvCastDTO;
import com.licenta.server.dto.TvCrewDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TvCreditsDTO {
    private List<TvCastDTO> cast;
    private List<TvCrewDTO> crew;
}
