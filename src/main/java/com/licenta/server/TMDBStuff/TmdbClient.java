package com.licenta.server.TMDBStuff;

import com.licenta.server.exception.TmdbNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class TmdbClient {
    private final WebClient webClient;
    //======//
    //Movies//
    //======//
    public TmdbMovieDto getMovieDetails(int id){
        return webClient.get().uri(uriBuilder ->
                            uriBuilder.path("/movie/" + id)
                                    .queryParam("language", "en-US")
                                    .queryParam("append_to_response", "credits")
                                    .build())
                    .retrieve()
                .onStatus(status -> status == HttpStatus.NOT_FOUND, response -> Mono.error(new TmdbNotFoundException("Movie show not found in TMDB")))
                .onStatus(HttpStatusCode::is4xxClientError , response ->
                        Mono.error(new RuntimeException("Tmdb Client Error")))
                .onStatus(HttpStatusCode::is5xxServerError , response -> Mono.error(new RuntimeException("Tmdb Server Error")))
                .bodyToMono(TmdbMovieDto.class).block();
    }
    @Cacheable(value = "cast" , key = "#id")
    public CreditsDTO getMovieCastAndCrew(int id){
        return webClient.get().uri(uriBuilder ->
                        uriBuilder.path("/movie/" + id + "/credits")
                                .queryParam("language", "en-US")
                                .build())
                .retrieve()
                .onStatus(status -> status == HttpStatus.NOT_FOUND, response -> Mono.error(new TmdbNotFoundException("Movie show not found in TMDB")))
                .onStatus(HttpStatusCode::is4xxClientError , response ->
                        Mono.error(new RuntimeException("Tmdb Client Error")))
                .onStatus(HttpStatusCode::is5xxServerError , response -> Mono.error(new RuntimeException("Tmdb Server Error")))
                .bodyToMono(CreditsDTO.class).block();
    }
    public TmdbPagedResponse<TmdbMovieCardDto> searchMovieByTitle(int page, String query) {
        return webClient.get().uri(uriBuilder ->
                uriBuilder.path("/search/movie")
                        .queryParam("language", "en-US")
                        .queryParam("query", query)
                        .queryParam("include_adult", true)
                        .queryParam("page", page)
                        .build()).retrieve().bodyToMono(new ParameterizedTypeReference<TmdbPagedResponse<TmdbMovieCardDto>>() {}).block();
    }
    public TmdbPagedResponse<TmdbMovieCardDto> getNowPlayingMovies(int page) {
        return webClient.get().uri(uriBuilder ->
                uriBuilder.path("/movie/now_playing")
                        .queryParam("language" , "en-US")
                        .queryParam("page" , page).build()).retrieve().bodyToMono(new ParameterizedTypeReference<TmdbPagedResponse<TmdbMovieCardDto>>() {}).block();
    }
    public TmdbPagedResponse<TmdbMovieCardDto> getPopularMovies(int page) {
        return webClient.get().uri(uriBuilder ->
                uriBuilder.path("/movie/popular")
                        .queryParam("language" , "en-US")
                        .queryParam("page" , page).build()).retrieve().bodyToMono(new ParameterizedTypeReference<TmdbPagedResponse<TmdbMovieCardDto>>() {}).block();
    }
    public TmdbPagedResponse<TmdbMovieCardDto> getUpcomingMovies(int page) {
        return webClient.get().uri(uriBuilder ->
                uriBuilder.path("/movie/upcoming")
                        .queryParam("language" , "en-US")
                        .queryParam("page" , page).build()).retrieve().bodyToMono(new ParameterizedTypeReference<TmdbPagedResponse<TmdbMovieCardDto>>() {}).block();
    }

    //========//
    //Tv Shows//
    //========//

    public TmdbTvDto getTvShowDetails(Integer id) {
        return webClient.get().uri(uriBuilder ->
                        uriBuilder.path("/tv/" + id)
                                .queryParam("language" , "en-US")
                                .queryParam("append_to_response", "aggregate_credits")
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
                ).bodyToMono(TmdbTvDto.class).block();
    }

    public TmdbPagedResponse<TmdbTvCardDto> searchTvByTitle(String query , int page){
        return webClient.get().uri(uriBuilder ->
                uriBuilder.path("/search/tv")
                        .queryParam("language" , "en-US")
                        .queryParam("query" , query)
                        .queryParam("page" , page)
                        .build()).retrieve().bodyToMono(new ParameterizedTypeReference<TmdbPagedResponse<TmdbTvCardDto>>() {}).block();
    }

    public TmdbPagedResponse<TmdbTvCardDto> popularTvShows(int page){
        return webClient.get().uri(uriBuilder ->
                uriBuilder.path("/tv/popular")
                        .queryParam("language" , "en-US")
                        .queryParam("page", page).build()).retrieve().bodyToMono(new ParameterizedTypeReference<TmdbPagedResponse<TmdbTvCardDto>>() {}).block();
    }
    public TmdbPagedResponse<TmdbTvCardDto> topRatedTvShow(int page){
        return webClient.get().uri(uriBuilder ->
                uriBuilder.path("/tv/top_rated")
                        .queryParam("language" , "en-US")
                        .queryParam("page", page).build()).retrieve().bodyToMono(new ParameterizedTypeReference<TmdbPagedResponse<TmdbTvCardDto>>() {}).block();
    }
    public TmdbPagedResponse<TmdbTvCardDto> onTheAir(int page){
        return webClient.get().uri(uriBuilder ->
                uriBuilder.path("/tv/on_the_air")
                        .queryParam("language" , "en-US")
                        .queryParam("page", page).build()).retrieve().bodyToMono(new ParameterizedTypeReference<TmdbPagedResponse<TmdbTvCardDto>>() {}).block();
    }

    //===========//
    //==Seasons==//
    //===========//
    public TmdbSeasonDto getSeasonDetails(int seriesId , int seasonNumber){
        return webClient.get().uri(uriBuilder ->
                uriBuilder.path("/tv/" + seriesId + "/season/" + seasonNumber)
                        .queryParam("language" , "en-US")
                        .queryParam("series_id" , seriesId)
                        .queryParam("season_number" , seasonNumber).build()).retrieve()
                .onStatus(status -> status == HttpStatus.NOT_FOUND , response -> Mono.error(new TmdbNotFoundException("Season not found!")))
                .onStatus(
                        HttpStatusCode::is4xxClientError,
                        response -> Mono.error(
                                new RuntimeException("TMDB client error"))
                )
                .onStatus(
                        HttpStatusCode::is5xxServerError,
                        response -> Mono.error(new RuntimeException("Tmdb Server Error!"))
                ).bodyToMono(TmdbSeasonDto.class).block();
    }


}
