package com.licenta.server.repository;

import com.licenta.server.models.Media;
import com.licenta.server.models.MediaType;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface MediaRepository extends JpaRepository<Media, Long> {
    Optional<Media> findMediaByMediaTypeAndTmdbId(MediaType mediaType, int id);
    @Query("""
        select m
        from Media m
        where m.lastSyncedAt is null
           or m.lastSyncedAt < :cutoff
        order by m.lastSyncedAt asc nulls first
    """)
    List<Media> findStaleInLibraries(@Param("cutoff") Instant cutoff, PageRequest of);
}
