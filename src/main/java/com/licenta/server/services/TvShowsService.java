package com.licenta.server.services;

import com.licenta.server.TMDBStuff.TmdbGenreDTO;
import com.licenta.server.TMDBStuff.TmdbTV.TmdbTvDTO;
import com.licenta.server.TMDBStuff.TmdbTV.TmdbTvHelper;
import com.licenta.server.TMDBStuff.TmdbTV.TvTmdbCardResults;
import com.licenta.server.TMDBStuff.TmdbTV.TvTmdbSeasonCard;
import com.licenta.server.dto.TvDTO;
import com.licenta.server.models.TvShows;
import com.licenta.server.repository.TvShowsRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TvShowsService {

    private final TmdbTvHelper tmdbTvHelper;
    private final TvShowsRepository tvShowsRepository;

    /* =========================
       MAPPERS
       ========================= */

    private TvDTO mapToDto(TvShows tv) {
        return TvDTO.builder()
                .tmdb_id(tv.getTmdb_id())
                .name(tv.getName())
                .posterPath(tv.getPosterPath())
                .overview(tv.getOverview())
                .releaseDate(tv.getReleaseDate())
                .genres(tv.getGenres())
                .numberOfSeasons(tv.getNumberOfSeasons())
                .backdropPath(tv.getBackdropPath())
                .voteCount(tv.getVoteCount())
                .voteAverage(tv.getVoteAverage())
                .status(tv.getStatus())
                .build();
    }

    private TvShows mapToEntity(TmdbTvDTO dto) {
        return TvShows.builder()
                .tmdb_id(dto.getId())
                .name(dto.getName())
                .posterPath(dto.getPosterPath())
                .overview(dto.getOverview())
                .releaseDate(dto.getAirDate())
                .genres(
                        dto.getGenres()
                                .stream()
                                .map(TmdbGenreDTO::getId)
                                .collect(Collectors.toList())
                )
                .numberOfSeasons(dto.getNumberOfSeasons())
                .backdropPath(dto.getBackdropPath())
                .voteCount(dto.getVoteCount())
                .voteAverage(dto.getVoteAverage())
                .status(dto.getStatus())
                .build();
    }

    /* =========================
       READ (NO DB WRITE)
       ========================= */

    public TvDTO getTvShowDetails(Integer tmdbId) {
        return tvShowsRepository.findById(tmdbId)
                .map(this::mapToDto)
                .orElseGet(() -> fetchFromTmdb(tmdbId));
    }

    private TvDTO fetchFromTmdb(Integer tmdbId) {
        TmdbTvDTO dto = tmdbTvHelper
                .getTmdbTvShowDetailsById(tmdbId);

        if (dto == null) {
            throw new RuntimeException("TV Show not found");
        }
        return mapToDto(mapToEntity(dto));
    }

    /* =========================
       WRITE (GET OR CREATE)
       ========================= */

    @Transactional
    public TvShows getOrCreateTvShow(Integer tmdbId) {
        return tvShowsRepository.findById(tmdbId)
                .orElseGet(() -> {
                    TmdbTvDTO dto = tmdbTvHelper
                            .getTmdbTvShowDetailsById(tmdbId);

                    if (dto == null) {
                        throw new RuntimeException("TV Show not found");
                    }

                    return tvShowsRepository.save(mapToEntity(dto));
                });
    }

    /* =========================
       SEASONS (READ ONLY FOR NOW)
       ========================= */

    public List<TvTmdbSeasonCard> getAllSeasonsFromTvShow(Integer tmdbId) {
        TmdbTvDTO dto = tmdbTvHelper
                .getTmdbTvShowDetailsById(tmdbId);
        if (dto == null) {
            throw new RuntimeException("TV Show not found");
        }

        return dto.getSeasons();
    }

    /* =========================
       SEARCH
       ========================= */

    public TvTmdbCardResults searchResults(String query, int page) {
        return tmdbTvHelper.searchResults(query, page);
    }

    public TvTmdbCardResults popular(int page){
        return tmdbTvHelper.popularNow(page);
    }
    public TvTmdbCardResults topRated(int page){
        return tmdbTvHelper.topRated(page);
    }
    public TvTmdbCardResults onTheAir(int page){
        return tmdbTvHelper.onTheAir(page);
    }
}
