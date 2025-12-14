package com.licenta.server.TMDBStuff;

import com.licenta.server.TMDBStuff.TmdbMovies.TmdbMovieDTO;
import com.licenta.server.TMDBStuff.TmdbMovies.TmdbCardResults;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.service.GenericResponseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.Map;

@Data
@RequiredArgsConstructor
@Service
public class TmdbHelper {
    private final WebClient tmdbClient;
    private final GenericResponseService responseBuilder;

    public TmdbCardResults searchTmdbMovieByTitle(int page, String query) {
            return tmdbClient.get().uri(uriBuilder ->
                    uriBuilder.path("/search/movie")
                            .queryParam("language", "en-US")
                            .queryParam("query", query)
                            .queryParam("include_adult", true)
                            .queryParam("page", page)
                            .build()).retrieve().bodyToMono(TmdbCardResults.class).block();
    }
    public ResponseEntity<TmdbMovieDTO> getTmdbMovieDetails(Integer tmdbId) {
        try {
            TmdbMovieDTO tmdbMovieDTO = tmdbClient.get().uri(uriBuilder ->
                            uriBuilder.path("/movie/" + tmdbId)
                                    .queryParam("language", "en-US")
                                    .build())
                    .retrieve().bodyToMono(TmdbMovieDTO.class).block();
            return ResponseEntity.ok(tmdbMovieDTO);
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
    public TmdbCardResults getTmdbNowPlayingMovies(int page) {
        return tmdbClient.get().uri(uriBuilder ->
                uriBuilder.path("/movie/now_playing")
                        .queryParam("language" , "en-US")
                        .queryParam("page" , page).build()).retrieve().bodyToMono(TmdbCardResults.class).block();
    }

    public TmdbCardResults getPopularMovies(int page) {
        return tmdbClient.get().uri(uriBuilder ->
                uriBuilder.path("/movie/popular")
                        .queryParam("language" , "en-US")
                        .queryParam("page" , page).build()).retrieve().bodyToMono(TmdbCardResults.class).block();
    }
    public TmdbCardResults getUpcomingMovies(int page) {
        return tmdbClient.get().uri(uriBuilder ->
                uriBuilder.path("/movie/upcoming")
                        .queryParam("language" , "en-US")
                        .queryParam("page" , page).build()).retrieve().bodyToMono(TmdbCardResults.class).block();
    }

}
