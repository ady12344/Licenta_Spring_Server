package com.licenta.server.services;

import com.licenta.server.TMDBStuff.TmdbClient;
import com.licenta.server.TMDBStuff.TmdbPagedResponse;
import com.licenta.server.TMDBStuff.TmdbTvCardDto;
import com.licenta.server.TMDBStuff.TmdbTvDto;
import com.licenta.server.dto.PagedResponseDto;
import com.licenta.server.dto.SeasonCardDto;
import com.licenta.server.dto.TvCardDto;
import com.licenta.server.dto.TvDto;
import com.licenta.server.mapper.MediaMapper;
import com.licenta.server.models.MediaType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TelevisionService {
    private final TmdbClient tmdbClient;
    public TmdbTvDto fetchTvShowTmdb(int id){return tmdbClient.getTvShowDetails(id);}

    public TvDto getTvDetails(int id){
        return  MediaMapper.mapTmdbToTvDto(fetchTvShowTmdb(id));
    }
    public PagedResponseDto<TvCardDto> searchTvShowByTitle(String query, int page ){
        TmdbPagedResponse<TmdbTvCardDto> tmdbRes = tmdbClient.searchTvByTitle(query, page);
        return new PagedResponseDto<TvCardDto>(tmdbRes.getPage(),
                tmdbRes.getResults().stream().map(MediaMapper::mapTmdbToTvCard).toList(),
                tmdbRes.getTotalPages());
    }
    public PagedResponseDto<TvCardDto> getPopularTvShows(int page ){
        TmdbPagedResponse<TmdbTvCardDto> tmdbRes = tmdbClient.popularTvShows(page);
        return new PagedResponseDto<TvCardDto>(tmdbRes.getPage(),
                tmdbRes.getResults().stream().map(MediaMapper::mapTmdbToTvCard).toList(),
                tmdbRes.getTotalPages());
    }
    public PagedResponseDto<TvCardDto> getTopRatedTvShows(int page ){
        TmdbPagedResponse<TmdbTvCardDto> tmdbRes = tmdbClient.topRatedTvShow(page);
        return new PagedResponseDto<TvCardDto>(tmdbRes.getPage(),
                tmdbRes.getResults().stream().map(MediaMapper::mapTmdbToTvCard).toList(),
                tmdbRes.getTotalPages());
    }
    public PagedResponseDto<TvCardDto> onTheAirTvShows(int page ){
        TmdbPagedResponse<TmdbTvCardDto> tmdbRes = tmdbClient.onTheAir(page);
        return new PagedResponseDto<TvCardDto>(tmdbRes.getPage(),
                tmdbRes.getResults().stream().map(MediaMapper::mapTmdbToTvCard).toList(),
                tmdbRes.getTotalPages());
    }

    public List<SeasonCardDto> getTvShowSeasons(int seriesId){
        return fetchTvShowTmdb(seriesId).getSeasons().stream().map(MediaMapper::mapTmdbSeasonCardToSeasonCardDto).toList();
    }
}
