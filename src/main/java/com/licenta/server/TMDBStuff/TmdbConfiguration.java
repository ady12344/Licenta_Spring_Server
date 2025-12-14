package com.licenta.server.TMDBStuff;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class TmdbConfiguration {
    @Value("${secret.TMDB.API.Key}")
    private String apiKey;
    @Bean
    public WebClient tmdbClient() {
        return WebClient.builder().baseUrl("https://api.themoviedb.org/3/")
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
                .defaultHeader(HttpHeaders.CONTENT_TYPE , "application/json;charset=UTF-8").build();

    }
}
