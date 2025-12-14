package com.licenta.server.repository;

import com.licenta.server.models.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface MovieRepository extends JpaRepository<Movie, Integer> {
    Optional<Movie> findByTmdbId(Integer tmdbId);
    //TO BE USED WHEN I DO THE LIBRARY
    Optional<List<Movie>> findByTmdbIdIn(List<Integer> movieIds);
}
