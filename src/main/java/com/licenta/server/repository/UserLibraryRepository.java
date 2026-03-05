package com.licenta.server.repository;

import com.licenta.server.models.UserLibrary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserLibraryRepository extends JpaRepository<UserLibrary, Long> {
    Page<UserLibrary> findByUserIdOrderByAddedAtDesc(Long userId, Pageable pageable);

    Page<UserLibrary> findByUserIdAndMediaTypeOrderByAddedAtDesc(Long userId, String mediaType, Pageable pageable);

    boolean existsByUserIdAndTmdbIdAndMediaType(Long userId, Integer tmdbId, String mediaType);

    void deleteByUserIdAndTmdbIdAndMediaType(Long userId, Integer tmdbId, String mediaType);
}
