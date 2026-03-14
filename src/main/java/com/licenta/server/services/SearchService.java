package com.licenta.server.services;

import com.licenta.server.TMDBStuff.TmdbClient;
import com.licenta.server.dto.MediaCardDTO;
import com.licenta.server.dto.PagedResponseDto;
import com.licenta.server.mapper.MediaMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class SearchService {
    private final TmdbClient tmdbClient;

    public PagedResponseDto<MediaCardDTO> search(String query, String type, int page) {

        if (type != null && type.equals("movie")) {
            var response = tmdbClient.searchMovieByTitle(page, query);
            List<MediaCardDTO> results = response.getResults().stream()
                    .map(MediaMapper::mapToMovieCard)
                    .toList();
            return new PagedResponseDto<>(page, results, response.getTotalPages());
        }

        if (type != null && type.equals("tv")) {
            var response = tmdbClient.searchTvByTitle(query, page);
            List<MediaCardDTO> results = response.getResults().stream()
                    .map(MediaMapper::mapTmdbToTvCard)
                    .toList();
            return new PagedResponseDto<>(page, results, response.getTotalPages());
        }

        // ALL — multi search
        var response = tmdbClient.searchMulti(query, page);
        List<MediaCardDTO> results = response.getResults().stream()
                .map(MediaMapper::mapMultiCardToDTO)
                .filter(Objects::nonNull)
                .toList();

        // if less than 20 results after filtering persons — pad with movie + tv search
        if (results.size() < 20) {
            var movieResponse = tmdbClient.searchMovieByTitle(page, query);
            var tvResponse    = tmdbClient.searchTvByTitle(query, page);

            List<MediaCardDTO> movieResults = movieResponse.getResults().stream()
                    .map(MediaMapper::mapToMovieCard).toList();
            List<MediaCardDTO> tvResults = tvResponse.getResults().stream()
                    .map(MediaMapper::mapTmdbToTvCard).toList();

            List<MediaCardDTO> combined = new ArrayList<>(results);

            movieResults.forEach(m -> {
                if (combined.stream().noneMatch(r -> r.getTmdbId() == m.getTmdbId()
                        && r.getMediaType() == m.getMediaType()))
                    combined.add(m);
            });

            tvResults.forEach(t -> {
                if (combined.stream().noneMatch(r -> r.getTmdbId() == t.getTmdbId()
                        && r.getMediaType() == t.getMediaType()))
                    combined.add(t);
            });
            int totalPages = Math.min(
                    Math.min(response.getTotalPages(), movieResponse.getTotalPages()),
                    Math.min(tvResponse.getTotalPages(), 500)
            );
            return new PagedResponseDto<>(page, combined, totalPages);
        }

        return new PagedResponseDto<>(page, results, response.getTotalPages());
    }

    // Discover — no query, optional genre + type filter
    public PagedResponseDto<MediaCardDTO> discover(String type, String genres, int page) {
        if (type != null && type.equals("tv")) {
            var response = tmdbClient.discoverTv(genres, page);
            List<MediaCardDTO> results = response.getResults().stream()
                    .map(MediaMapper::mapTmdbToTvCard)
                    .toList();
            return new PagedResponseDto<>(page, results, response.getTotalPages());
        }

        if (type != null && type.equals("movie")) {
            var response = tmdbClient.discoverMovies(genres, page);
            List<MediaCardDTO> results = response.getResults().stream()
                    .map(MediaMapper::mapToMovieCard)
                    .toList();
            return new PagedResponseDto<>(page, results, response.getTotalPages());
        }

        // both — fetch movies and tv, interleave them
        var movies = tmdbClient.discoverMovies(genres, page);
        var tv     = tmdbClient.discoverTv(genres, page);

        List<MediaCardDTO> movieCards = movies.getResults().stream()
                .map(MediaMapper::mapToMovieCard)
                .toList();
        List<MediaCardDTO> tvCards = tv.getResults().stream()
                .map(MediaMapper::mapTmdbToTvCard)
                .toList();

        // interleave: movie, tv, movie, tv...
        List<MediaCardDTO> combined = new ArrayList<>();
        int max = Math.max(movieCards.size(), tvCards.size());
        for (int i = 0; i < max; i++) {
            if (i < movieCards.size()) combined.add(movieCards.get(i));
            if (i < tvCards.size())    combined.add(tvCards.get(i));
        }

        int totalPages = Math.max(movies.getTotalPages(), tv.getTotalPages());
        return new PagedResponseDto<>(page, combined, totalPages);
    }
}