package com.licenta.server.services;

import com.licenta.server.TMDBStuff.*;
import com.licenta.server.dto.*;
import com.licenta.server.mapper.MediaMapper;
import com.licenta.server.models.MediaType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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
}
