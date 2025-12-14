package com.licenta.server.services;

import com.licenta.server.TMDBStuff.TmdbGenreDTO;
import com.licenta.server.TMDBStuff.TmdbTV.TmdbTvDTO;
import com.licenta.server.TMDBStuff.TmdbTV.TmdbTvHelper;
import com.licenta.server.TMDBStuff.TmdbTV.TvShowSeasonsCard;
import com.licenta.server.dto.TvDTO;
import com.licenta.server.models.TvShows;
import com.licenta.server.repository.TvShowsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TvShowsService {
    private final TmdbTvHelper tmdbTvHelper;
    private final TvShowsRepository tvShowsRepository;

    public TvDTO mapToDto(TvShows tv){
        return TvDTO.builder()
                .tmdb_id(tv.getTmdb_id())
                .name(tv.getName())
                .posterPath(tv.getPosterPath())
                .overview(tv.getOverview())
                .release_date(tv.getReleaseDate())
                .genres(tv.getGenres())
                .numberOfSeasons(tv.getNumberOfSeasons())
                .backdropPath(tv.getBackdropPath())
                .voteCount(tv.getVoteCount())
                .voteAverage(tv.getVoteAverage())
                .status(tv.getStatus())
                .build();
    }
    @Cacheable(value = "tvShowById" , key = "#id")
    public ResponseEntity<TvDTO> getTvShowById(Integer id) {
        var tvShows = tvShowsRepository.findById(id);
        if (tvShows.isPresent()) {
            TvShows tv = tvShows.get();
            return new ResponseEntity<>(mapToDto(tv), HttpStatus.OK);
        }

        ResponseEntity<TmdbTvDTO> response = tmdbTvHelper.getTmdbTvShowDetailsById(id);
        if (response.getStatusCode().is2xxSuccessful()) {
            TmdbTvDTO dto = response.getBody();
            TvDTO tvDTO = TvDTO.builder()
                    .tmdb_id(dto.getId())
                    .name(dto.getName())
                    .posterPath(dto.getPosterPath())
                    .overview(dto.getOverview())
                    .release_date(dto.getAirDate())
                    .genres(dto.getGenres().stream().mapToInt(TmdbGenreDTO::getId).boxed().collect(Collectors.toList()))
                    .numberOfSeasons(dto.getNumberOfSeasons())
                    .backdropPath(dto.getBackdropPath())
                    .voteCount(dto.getVoteCount())
                    .voteAverage(dto.getVoteAverage())
                    .status(dto.getStatus())
                    .build();
            return ResponseEntity.ok(tvDTO);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<List<TvShowSeasonsCard>> getAllSeasonsFromTvShow(Integer id) {
        ResponseEntity<TmdbTvDTO> response = tmdbTvHelper.getTmdbTvShowDetailsById(id);
        if(response.getStatusCode().is2xxSuccessful()) {
            return ResponseEntity.ok(response.getBody().getSeasons());
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
