package com.licenta.server.services;

import com.licenta.server.TMDBStuff.*;
import com.licenta.server.dto.*;
import com.licenta.server.mapper.MediaMapper;
import com.licenta.server.models.Media;
import com.licenta.server.models.MediaType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TelevisionService {
    private final TmdbClient tmdbClient;
    private static final Integer TMDB_MAX_PAGE_LIMIT = 500;
    public TmdbTvDto fetchTvShowTmdb(int id){return tmdbClient.getTvShowDetails(id);}

    public TvDto getTvDetails(int id){
        return  MediaMapper.mapTmdbToTvDto(fetchTvShowTmdb(id));
    }

    private PagedResponseDto<TvCardDto> mapToPagedResponse(TmdbPagedResponse<TmdbTvCardDto> tmdbRes) {
        int safeTotalPages = Math.min(tmdbRes.getTotalPages(), TMDB_MAX_PAGE_LIMIT);
        return new PagedResponseDto<>(
                tmdbRes.getPage(),
                tmdbRes.getResults().stream().map(MediaMapper::mapTmdbToTvCard).toList(),
                safeTotalPages
        );
    }

    public PagedResponseDto<TvCardDto> searchTvShowByTitle(String query, int page ){
        return mapToPagedResponse(tmdbClient.searchTvByTitle(query, page));
    }
    public PagedResponseDto<TvCardDto> getPopularTvShows(int page ){
        return mapToPagedResponse(tmdbClient.popularTvShows(page));
    }
    public PagedResponseDto<TvCardDto> getTopRatedTvShows(int page ){
        return mapToPagedResponse(tmdbClient.topRatedTvShow(page));
    }
    public PagedResponseDto<TvCardDto> onTheAirTvShows(int page ){
        return mapToPagedResponse(tmdbClient.onTheAir(page));
    }

    public CreditsDTO<TvCastDTO , TvCrewDTO> getTvShowCastAndCrew(int id){
        return tmdbClient.getTvShowCastAndCrew(id);
    }

    public PagedResponseDto<TvCastDTO> getTvShowCastPaged(int id, int page, int size) {
        // 1. Obținem toate datele de la TMDB (folosind metoda deja existentă în service)
        CreditsDTO<TvCastDTO, TvCrewDTO> allCredits = getTvShowCastAndCrew(id);

        if (allCredits == null || allCredits.getCast() == null) {
            return PagedResponseDto.<TvCastDTO>builder()
                    .page(page)
                    .results(List.of())
                    .totalPages(0)
                    .build();
        }

        // 2. Extragem lista de cast și o sortăm după importanță (order)
        List<TvCastDTO> fullCast = allCredits.getCast().stream()
                .sorted(Comparator.comparingInt(TvCastDTO::getOrder))
                .map(cast -> {
                    cast.setProfilePath(MediaMapper.buildTmdbImageUrl(cast.getProfilePath() , "w500"));
                    return cast;
                })
                .collect(Collectors.toList());

        int totalResults = fullCast.size();

        // 3. Calculăm totalPages (ex: 45 actori / 10 per pagină = 5 pagini)
        int totalPages = (int) Math.ceil((double) totalResults / size);

        // 4. Calculăm limitele pentru pagina curentă (evităm IndexOutOfBounds)
        // page ar trebui să fie indexat de la 0 în logica subList
        int start = Math.min(page * size, totalResults);
        int end = Math.min(start + size, totalResults);

        // 5. Tăiem felia corespunzătoare paginii
        List<TvCastDTO> pagedResults = fullCast.subList(start, end);

        // 6. Returnăm obiectul PagedResponseDto
        return PagedResponseDto.<TvCastDTO>builder()
                .page(page)
                .results(pagedResults)
                .totalPages(totalPages)
                .build();
    }
}
