package com.licenta.server.services;
import com.licenta.server.TMDBStuff.TmdbGenreDTO;
import com.licenta.server.TMDBStuff.TmdbMovies.TmdbMovieHelper;
import com.licenta.server.TMDBStuff.TmdbMovies.TmdbMovieDTO;
import com.licenta.server.TMDBStuff.TmdbMovies.TmdbMovieCardResults;
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
    private final TmdbMovieHelper tmdbHelper;
    //#IMPORTANT
    //GET A LIST OF USERMOVIE ID'S TO GET THE MOVIE DETAILS FOR THE LIBRARY
    //AND MAKE A DB FUNCTION TO RETURN A LIST OF MOVIES BASED ON THE ID'S RECIEVED
    public Movie mapToEntity(MovieDTO movieDTO) {
        return Movie.builder()
                .tmdbId(movieDTO.getTmdbId())
                .title(movieDTO.getTitle())
                .rating(movieDTO.getRating())
                .genres(movieDTO.getGenres())
                .overview(movieDTO.getOverview())
                .releaseDate(movieDTO.getReleaseDate())
                .voteCount(movieDTO.getVoteCount())
                .backdropPath(movieDTO.getBackdropPath())
                .posterPath(movieDTO.getPosterPath())
                .build();
    }
    public MovieDTO mapToDTO(Movie movie) {
        return MovieDTO.builder()
                .tmdbId(movie.getTmdbId())
                .title(movie.getTitle())
                .backdropPath(movie.getBackdropPath())
                .genres(movie.getGenres())
                .voteCount(movie.getVoteCount())
                .overview(movie.getOverview())
                .posterPath(movie.getPosterPath())
                .rating(movie.getRating())
                .releaseDate(movie.getReleaseDate())
                .build();
    }

    public void addMovie(Integer id) {
        if(!movieRepository.existsById(id)){
            MovieDTO movieDTO = getMovieById(id).getBody();
            Movie movie =  mapToEntity(movieDTO);
            movieRepository.save(movie);
        }
    }
    public ResponseEntity<TmdbMovieCardResults> searchMovieByTitle(int page , String query) {
            return ResponseEntity.ok().body(tmdbHelper.searchTmdbMovieByTitle(page, query));
    }
    @Cacheable(value = "nowPlayingMovies" , key = "#page")
    public ResponseEntity<TmdbMovieCardResults> getNowPlayingMovies(int page) {
        return ResponseEntity.ok().body(tmdbHelper.getTmdbNowPlayingMovies(page));
    }

    @Cacheable(value = "popularMovies" , key = "#page")
    public ResponseEntity<TmdbMovieCardResults> getPopularMovies(int page) {
        return ResponseEntity.ok().body(tmdbHelper.getPopularMovies(page));
    }
    public ResponseEntity<TmdbMovieCardResults> getUpcomingMovies(int page) {
        return ResponseEntity.ok().body(tmdbHelper.getUpcomingMovies(page));
    }
    @Cacheable(value = "movieById" , key = "#id")
    public ResponseEntity<MovieDTO> getMovieById(Integer id) {
       var movieInDB =  movieRepository.findByTmdbId(id);
        if(movieInDB.isPresent()) {
           Movie movie =  movieInDB.get();
           MovieDTO movieDTO = mapToDTO(movie);
            return ResponseEntity.ok().body(movieDTO);
        }
        ResponseEntity<TmdbMovieDTO> response = tmdbHelper.getTmdbMovieDetails(id);
        if(response.getStatusCode().is2xxSuccessful()) {
            TmdbMovieDTO dto = response.getBody();
            MovieDTO movie= MovieDTO.builder()
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
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}
