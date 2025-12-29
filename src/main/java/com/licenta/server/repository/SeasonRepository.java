package com.licenta.server.repository;

import com.licenta.server.models.Season;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SeasonRepository extends JpaRepository<Season, Long> {
    Optional<Season> findSeasonBySeriesIdAndSeasonNumber(int seriesId, int seasonNumber);

    Boolean existsBySeriesIdAndSeasonNumber(int seriesId, int seasonNumber);
}
