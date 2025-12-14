package com.licenta.server.services;
import com.licenta.server.TMDBStuff.TmdbMovies.TmdbGenreDTO;
import com.licenta.server.TMDBStuff.TmdbHelper;
import com.licenta.server.TMDBStuff.TmdbMovies.TmdbMovieDTO;
import com.licenta.server.TMDBStuff.TmdbMovies.TmdbCardResults;
import com.licenta.server.dto.MovieDTO;
import com.licenta.server.models.Movie;
import com.licenta.server.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MovieService {
    private final MovieRepository movieRepository;
    private final TmdbHelper tmdbHelper;
    //#IMPORTANT
    //GET A LIST OF USERMOVIE ID'S TO GET THE MOVIE DETAILS FOR THE LIBRARY
    //AND MAKE A DB FUNCTION TO RETURN A LIST OF MOVIES BASED ON THE ID'S RECIEVED
    public void addMovie(MovieDTO dto) {
        Movie movie = movieRepository.findByTmdbId(dto.getTmdbId())
                        .orElse(new Movie());

                movie.setTmdbId(dto.getTmdbId());
                movie.setGenres(dto.getGenres());
                movie.setRating(dto.getRating());
                movie.setBackdropPath(dto.getBackdropPath());
                movie.setTitle(dto.getTitle());
                movie.setOverview(dto.getOverview());
                movie.setPosterPath(dto.getPosterPath());
                movie.setReleaseDate(dto.getReleaseDate());
                movie.setVoteCount(dto.getVoteCount());
        movieRepository.save(movie);
    }
    public ResponseEntity<TmdbCardResults> searchMovieByTitle(int page , String query) {
            return ResponseEntity.ok().body(tmdbHelper.searchTmdbMovieByTitle(page, query));
    }
    @Cacheable(value = "nowPlayingMovies" , key = "#page")
    public ResponseEntity<TmdbCardResults> getNowPlayingMovies(int page) {
        return ResponseEntity.ok().body(tmdbHelper.getTmdbNowPlayingMovies(page));
    }

    @Cacheable(value = "popularMovies" , key = "#page")
    public ResponseEntity<TmdbCardResults> getPopularMovies(int page) {
        return ResponseEntity.ok().body(tmdbHelper.getPopularMovies(page));
    }
    public ResponseEntity<TmdbCardResults> getUpcomingMovies(int page) {
        return ResponseEntity.ok().body(tmdbHelper.getUpcomingMovies(page));
    }
    @Cacheable(value = "movieById" , key = "#id")
    public ResponseEntity<Movie> getMovieById(Integer id) {
       var movieInDB =  movieRepository.findByTmdbId(id);
        if(movieInDB.isPresent()) {
            Movie movie = movieInDB.get();
            System.out.println("Coming from DB");
            return ResponseEntity.ok().body(movie);
        }
        ResponseEntity<TmdbMovieDTO> response = tmdbHelper.getTmdbMovieDetails(id);
        if(response.getStatusCode().is2xxSuccessful()) {
            TmdbMovieDTO dto = response.getBody();
            Movie movie= Movie.builder()
                    .tmdbId(dto.getTmdbId())
                    .title(dto.getTitle())
                    .backdropPath(dto.getBackdropPath())
                    .genres(dto.getGenres().stream().mapToInt(TmdbGenreDTO::getId).boxed().collect(Collectors.toList()))
                    .voteCount(dto.getVoteCount())
                    .overview(dto.getOverview())
                    .posterPath(dto.getPosterPath())
                    .rating(dto.getRating())
                    .releaseDate(dto.getReleaseDate())
                    .build();
            System.out.println("Coming from tmdb");
            return ResponseEntity.ok().body(movie);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
