package com.licenta.server.repository;

import com.licenta.server.models.TvShows;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TvShowsRepository extends JpaRepository<TvShows, Integer> {
}
