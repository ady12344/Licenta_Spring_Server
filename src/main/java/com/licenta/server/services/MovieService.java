package com.licenta.server.services;

import com.licenta.server.TMDBStuff.*;
import com.licenta.server.dto.*;
import com.licenta.server.mapper.MediaMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MovieService {
    private final TmdbClient tmdbClient;
    private static final Integer TMDB_MAX_PAGE_LIMIT = 500;
    public TmdbMovieDto fetchMovieFromTmdb(int id){
        return tmdbClient.getMovieDetails(id);
    }
    public MovieDto getMovieDetails(int id){
        return MediaMapper.newMapToMovieDto(fetchMovieFromTmdb(id));

    }
    private PagedResponseDto<MovieCardDto> mapToPagedResponse(TmdbPagedResponse<TmdbMovieCardDto> tmdbRes) {
        int safeTotalPages = Math.min(tmdbRes.getTotalPages(), TMDB_MAX_PAGE_LIMIT);
        return new PagedResponseDto<>(
                tmdbRes.getPage(),
                tmdbRes.getResults().stream().map(MediaMapper::mapToMovieCard).toList(),
                safeTotalPages
        );
    }

    public PagedResponseDto<MovieCardDto> searchMovieByTitle(int page , String query){
        return mapToPagedResponse(tmdbClient.searchMovieByTitle(page , query));
    }

    public PagedResponseDto<MovieCardDto> getPopularMovies(int page){
     return mapToPagedResponse(tmdbClient.getPopularMovies(page));
    }
    public PagedResponseDto<MovieCardDto> getNowPlayingMovies(int page){
        return mapToPagedResponse(tmdbClient.getNowPlayingMovies(page));

    }
    public PagedResponseDto<MovieCardDto> getUpcomingMovies(int page){
        return mapToPagedResponse(tmdbClient.getUpcomingMovies(page));
    }
    //@Cacheable(value = "cast" , key = "#id")
    public CreditsDTO<CastDTO, CrewDTO> getMovieCastAndCrew(int id){
        //System.out.println("DEBUG: Se face apel real la TMDB pentru ID: " + id);
        return tmdbClient.getMovieCastAndCrew(id);
    }

    //test zone
    /*public PagedResponseDto<CastDTO> getMovieCastPaged(int id, int page, int size) {
        // 1. Obținem toate datele de la TMDB (metoda ta care folosește WebClient)
        CreditsDTO<CastDTO , CrewDTO> allCredits = getMovieCastAndCrew(id);

        // 2. Extragem lista de cast și o sortăm după importanță (order)
        List<CastDTO> fullCast = allCredits.getCast().stream()
                .sorted(Comparator.comparingInt(CastDTO::getOrder))
                .collect(Collectors.toList());

        int totalResults = fullCast.size();

        // 3. Calculăm totalPages (ex: 45 actori / 10 per pagină = 5 pagini)
        int totalPages = (int) Math.ceil((double) totalResults / size);

        // 4. Calculăm limitele pentru pagina curentă (evităm IndexOutOfBounds)
        int start = Math.min(page * size, totalResults);
        int end = Math.min(start + size, totalResults);

        // 5. Tăiem felia corespunzătoare paginii
        List<CastDTO> pagedResults = fullCast.subList(start, end);

        // 6. Returnăm obiectul  PagedResponseDto
        return PagedResponseDto.<CastDTO>builder()
                .page(page)
                .results(pagedResults)
                .totalPages(totalPages)
                .build();
    }*/
    public PagedResponseDto<CastDTO> getMovieCastPaged(int id, int page, int size) {
        // 1. Obținem datele (din TmdbClient care are @Cacheable)
        CreditsDTO<CastDTO, CrewDTO> allCredits = getMovieCastAndCrew(id);

        if (allCredits == null || allCredits.getCast() == null) {
            return PagedResponseDto.<CastDTO>builder().results(List.of()).build();
        }

        // 2. Extragem, SORTĂM și PROCESĂM URL-urile
        List<CastDTO> fullCast = allCredits.getCast().stream()
                .sorted(Comparator.comparingInt(CastDTO::getOrder))
                .map(cast -> {
                    cast.setProfilePath(MediaMapper.buildTmdbImageUrl(cast.getProfilePath(), "w500"));
                    return cast;
                })
                .collect(Collectors.toList());

        int totalResults = fullCast.size();
        int totalPages = (int) Math.ceil((double) totalResults / size);

        int start = Math.min(page * size, totalResults);
        int end = Math.min(start + size, totalResults);

        List<CastDTO> pagedResults = fullCast.subList(start, end);

        return PagedResponseDto.<CastDTO>builder()
                .page(page)
                .results(pagedResults)
                .totalPages(totalPages)
                .build();
    }
    // test


    public TmdbMovieDto test(int id){
        return fetchMovieFromTmdb(id);
    }
}
