package com.licenta.server.services;

import com.licenta.server.TMDBStuff.*;
import com.licenta.server.dto.*;

import com.licenta.server.mapper.MediaMapper;
import com.licenta.server.models.Media;
import com.licenta.server.models.MediaType;
import com.licenta.server.repository.MediaRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.Null;
import lombok.RequiredArgsConstructor;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MediaService {
    private final MediaRepository mediaRepository;
    private final TmdbClient tmdbClient;
    private MediaMapper mapper;
    private boolean isStale(Media media) {
        return media.getLastSyncedAt() == null ||
                media.getLastSyncedAt().isBefore(Instant.now().minus(Duration.ofDays(7)));
    }

    private LocalDate parseDate(String s) {
        if (s == null || s.isBlank()) return null;
        return LocalDate.parse(s); // TMDB gives yyyy-MM-dd
    }


    //=======//
    //Movies//
    //======//
    public TmdbMovieDto fetchMovieFromTmdb(int id){
        return tmdbClient.getMovieDetails(id);
    }

    //Maybe i can refactor this to be centralized? Just one media and switch with case of media type of TV or Movie
    public MovieDto getMovieDetails(int id){
        return mediaRepository.findMediaByMediaTypeAndTmdbId(MediaType.MOVIE ,id)
                .map(MediaMapper::mapMediaToMovieDto).orElseGet(() -> mapper.mapTmdbToMovieDto(fetchMovieFromTmdb(id)));

    }
    @Transactional
    public Media upsertAndSyncMedia(MediaType type, int tmdbId) {

        Media media = mediaRepository.findMediaByMediaTypeAndTmdbId(type, tmdbId)
                .orElseGet(Media::new);

        media.setMediaType(type);
        media.setTmdbId(tmdbId);

        switch (type) {
            case MOVIE -> {
                TmdbMovieDto dto = tmdbClient.getMovieDetails(tmdbId);

                media.setTitle(dto.getTitle());
                media.setOverview(dto.getOverview());
                media.setPosterPath(dto.getPosterPath());
                media.setBackdropPath(dto.getBackdropPath());
                media.setVoteAverage(dto.getVoteAverage());
                media.setReleaseDate(parseDate(dto.getReleaseDate()));
                media.setFirstAirDate(null);

                media.setGenres(dto.getGenres() == null ? List.of()
                        : dto.getGenres().stream().map(TmdbGenreDto::getName).toList());
            }
            case TV -> {
                TmdbTvDto dto = tmdbClient.getTvShowDetails(tmdbId);

                media.setTitle(dto.getName());
                media.setOverview(dto.getOverview());
                media.setPosterPath(dto.getPosterPath());
                media.setBackdropPath(dto.getBackdropPath());
                media.setVoteAverage(dto.getVoteAverage());
                media.setFirstAirDate(parseDate(dto.getFirstAirDate()));
                media.setStatus(dto.getStatus());
                media.setReleaseDate(null);
                media.setNumberOfSeasons(dto.getNumberOfSeasons());

                media.setGenres(dto.getGenres() == null ? List.of()
                        : dto.getGenres().stream().map(TmdbGenreDto::getName).toList());
            }
            default -> throw new IllegalArgumentException("Unsupported media type: " + type);
        }

        media.setLastSyncedAt(Instant.now());
        return mediaRepository.save(media);
    }

    public PagedResponseDto<MovieCardDto> searchMovieByTitle(int page , String query){
        TmdbPagedResponse<TmdbMovieCardDto> tmdbRes = tmdbClient.searchMovieByTitle(page , query);
        return new PagedResponseDto<MovieCardDto>(tmdbRes.getPage(),
                tmdbRes.getResults().stream().map(MediaMapper::mapToMovieCard).toList() ,
                tmdbRes.getTotalPages());
    }

    public PagedResponseDto<MovieCardDto> getPopularMovies(int page){
        TmdbPagedResponse<TmdbMovieCardDto> tmdbRes = tmdbClient.getPopularMovies(page);
        return new PagedResponseDto<MovieCardDto>(tmdbRes.getPage(),
                tmdbRes.getResults().stream().map(MediaMapper::mapToMovieCard).toList() ,
                tmdbRes.getTotalPages());
    }
    public PagedResponseDto<MovieCardDto> getNowPlayingMovies(int page){
        TmdbPagedResponse<TmdbMovieCardDto> tmdbRes = tmdbClient.getNowPlayingMovies(page);
        return new PagedResponseDto<MovieCardDto>(tmdbRes.getPage(),
                tmdbRes.getResults().stream().map(MediaMapper::mapToMovieCard).toList() ,
                tmdbRes.getTotalPages());
    }
    public PagedResponseDto<MovieCardDto> getUpcomingMovies(int page){
        TmdbPagedResponse<TmdbMovieCardDto> tmdbRes = tmdbClient.getUpcomingMovies(page);
        return new PagedResponseDto<MovieCardDto>(tmdbRes.getPage(),
                tmdbRes.getResults().stream().map(MediaMapper::mapToMovieCard).toList() ,
                tmdbRes.getTotalPages());
    }
    //============//
    //==TV SHOWS==//
    //============//
    public TmdbTvDto fetchTvShowTmdb(int id){return tmdbClient.getTvShowDetails(id);}

    public TvDto getTvDetails(int id){
        return mediaRepository.findMediaByMediaTypeAndTmdbId(MediaType.TV , id)
                .map(MediaMapper::mapMediaToTvDto).orElseGet(() -> mapper.mapTmdbToTvDto(fetchTvShowTmdb(id)));
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
}
