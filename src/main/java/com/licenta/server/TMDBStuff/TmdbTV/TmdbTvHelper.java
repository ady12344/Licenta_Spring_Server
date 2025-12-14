package com.licenta.server.TMDBStuff.TmdbTV;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
@RequiredArgsConstructor
@Service
public class TmdbTvHelper {
    private final WebClient tmdbClient;

    public ResponseEntity<TmdbTvDTO> getTmdbTvShowDetailsById(Integer id) {
        try{
            TmdbTvDTO tmdbTvDTO = tmdbClient.get().uri(uriBuilder ->
                    uriBuilder.path("/tv/" + id)
                            .queryParam("language" , "en-US")
                            .build()).retrieve().bodyToMono(TmdbTvDTO.class).block();
            return ResponseEntity.ok(tmdbTvDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
