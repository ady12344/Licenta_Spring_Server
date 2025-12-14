package com.licenta.server.repository;

import com.licenta.server.models.UserMovies;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserMoviesRepository extends JpaRepository<UserMovies,Long> {
    Optional<UserMovies> findByUserIdAndMovieId(Long userId, Integer movieId);
}
