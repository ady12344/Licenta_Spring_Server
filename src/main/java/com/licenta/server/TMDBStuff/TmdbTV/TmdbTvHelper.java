package com.licenta.server.TMDBStuff.TmdbTV;
import com.licenta.server.exception.TmdbNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class TmdbTvHelper {
    private final WebClient tmdbClient;

    public TmdbTvDTO getTmdbTvShowDetailsById(Integer id) {
        return tmdbClient.get().uri(uriBuilder ->
                uriBuilder.path("/tv/" + id)
                        .queryParam("language" , "en-US")
                        .build()).retrieve()
                .onStatus(status -> status == HttpStatus.NOT_FOUND, response -> Mono.error(new TmdbNotFoundException("TV show not found in TMDB")))
                .onStatus( HttpStatusCode::is4xxClientError,
                        response -> Mono.error(
                                new RuntimeException("TMDB client error")))
                .onStatus(
                        HttpStatusCode::is5xxServerError,
                        response -> Mono.error(
                                new RuntimeException("TMDB server error")
                        )
                ).bodyToMono(TmdbTvDTO.class).block();
    }

    public TvTmdbCardResults searchResults(String query , int page){
        return tmdbClient.get().uri(uriBuilder ->
                uriBuilder.path("/search/tv")
                        .queryParam("query" , query)
                        .queryParam("page" , page)
                        .build()).retrieve().bodyToMono(TvTmdbCardResults.class).block();
    }

    public TvTmdbCardResults popularNow(int page){
        return tmdbClient.get().uri(uriBuilder ->
                uriBuilder.path("/tv/popular")
                        .queryParam("page", page).build()).retrieve().bodyToMono(TvTmdbCardResults.class).block();
    }
    public TvTmdbCardResults topRated(int page){
        return tmdbClient.get().uri(uriBuilder ->
                uriBuilder.path("/tv/top_rated")
                        .queryParam("page", page).build()).retrieve().bodyToMono(TvTmdbCardResults.class).block();
    }
    public TvTmdbCardResults onTheAir(int page){
        return tmdbClient.get().uri(uriBuilder ->
                uriBuilder.path("/tv/on_the_air")
                        .queryParam("page", page).build()).retrieve().bodyToMono(TvTmdbCardResults.class).block();
    }
}
