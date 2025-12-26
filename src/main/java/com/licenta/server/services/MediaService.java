package com.licenta.server.services;

import com.licenta.server.TMDBStuff.*;
import com.licenta.server.dto.MovieCardDto;
import com.licenta.server.dto.MovieDto;

import com.licenta.server.dto.PagedResponseDto;
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

    private boolean isStale(Media media) {
        return media.getLastSyncedAt() == null ||
                media.getLastSyncedAt().isBefore(Instant.now().minus(Duration.ofDays(7)));
    }

    public Media mapToEntity(MovieDto dto){
        return Media.builder()
                .tmdbId(dto.getTmdbId())
                .title(dto.getTitle())
                .backdropPath(dto.getBackdropPath())
                .genres(dto.getGenres())
                .overview(dto.getOverview())
                .voteAverage(dto.getTmdbRating())
                .releaseDate(dto.getReleaseDate() == null ? null : LocalDate.parse(dto.getReleaseDate()))
                .popularity(dto.getPopularity())
                .posterPath(dto.getPosterPath())
                .mediaType(dto.getMediaType())
                .build();
    }

    private LocalDate parseDate(String s) {
        if (s == null || s.isBlank()) return null;
        return LocalDate.parse(s); // TMDB gives yyyy-MM-dd
    }

    public MovieDto mapMediaToMovieDto(Media media){
        return MovieDto.builder()
                .tmdbId(media.getTmdbId())
                .title(media.getTitle())
                .backdropPath(media.getBackdropPath())
                .genres(media.getGenres())
                .overview(media.getOverview())
                .tmdbRating(media.getVoteAverage())
                .releaseDate(media.getReleaseDate() == null ? null : media.getReleaseDate().toString())
                .popularity(media.getPopularity())
                .posterPath(media.getPosterPath())
                .mediaType(MediaType.MOVIE)
                .build();
    }
    public MovieDto mapTmdbToMovieDto(TmdbMovieDto dto){
        return MovieDto.builder()
                .tmdbId(dto.getId())
                .title(dto.getTitle())
                .backdropPath(dto.getBackdropPath())
                .genres(dto.getGenres() == null ? List.of() : dto.getGenres().stream().map(TmdbGenreDto::getName).toList())
                .overview(dto.getOverview())
                .tmdbRating(dto.getVoteAverage())
                .releaseDate(dto.getReleaseDate())
                .popularity(dto.getPopularity())
                .posterPath(dto.getPosterPath())
                .mediaType(MediaType.MOVIE)
                .build();
    }
    public MovieCardDto mapToMovieCard(TmdbMovieCardDto movieCardDto){
        return MovieCardDto.builder()
                .title(movieCardDto.getTitle())
                .posterPath(movieCardDto.getPosterPath())
                .tmdbId(movieCardDto.getTmdbId()).build();
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
                .map(this::mapMediaToMovieDto).orElseGet(() -> mapTmdbToMovieDto(fetchMovieFromTmdb(id)));

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
                media.setPopularity(dto.getPopularity());
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
                media.setPopularity(dto.getPopularity());
                media.setVoteAverage(dto.getVoteAverage());
                media.setFirstAirDate(parseDate(dto.getFirstAirDate()));
                media.setReleaseDate(null);

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
                tmdbRes.getResults().stream().map(this::mapToMovieCard).toList() ,
                tmdbRes.getTotalPages());
    }

    public PagedResponseDto<MovieCardDto> getPopularMovies(int page){
        TmdbPagedResponse<TmdbMovieCardDto> tmdbRes = tmdbClient.getPopularMovies(page);
        return new PagedResponseDto<MovieCardDto>(tmdbRes.getPage(),
                tmdbRes.getResults().stream().map(this::mapToMovieCard).toList() ,
                tmdbRes.getTotalPages());
    }
    public PagedResponseDto<MovieCardDto> getNowPlayingMovies(int page){
        TmdbPagedResponse<TmdbMovieCardDto> tmdbRes = tmdbClient.getNowPlayingMovies(page);
        return new PagedResponseDto<MovieCardDto>(tmdbRes.getPage(),
                tmdbRes.getResults().stream().map(this::mapToMovieCard).toList() ,
                tmdbRes.getTotalPages());
    }
    public PagedResponseDto<MovieCardDto> getUpcomingMovies(int page){
        TmdbPagedResponse<TmdbMovieCardDto> tmdbRes = tmdbClient.getUpcomingMovies(page);
        return new PagedResponseDto<MovieCardDto>(tmdbRes.getPage(),
                tmdbRes.getResults().stream().map(this::mapToMovieCard).toList() ,
                tmdbRes.getTotalPages());
    }


}
