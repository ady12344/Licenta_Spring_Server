package com.licenta.server.services;

import com.licenta.server.dto.LibraryItemDTO;
import com.licenta.server.dto.MediaCardDTO;
import com.licenta.server.dto.PagedResponseDto;
import com.licenta.server.exception.ItemNotFoundException;
import com.licenta.server.models.MediaType;
import com.licenta.server.models.User;
import com.licenta.server.models.UserLibrary;
import com.licenta.server.repository.UserLibraryRepository;
import com.licenta.server.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserLibraryService {
    private final UserLibraryRepository libraryRepository;
    private final UserRepository userRepository;

    @Transactional
    public void addToLibrary(String username, MediaCardDTO dto) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found!"));

        if (libraryRepository.existsByUserIdAndTmdbIdAndMediaType(
                user.getId(), dto.getTmdbId(), dto.getMediaType())) {
            throw new RuntimeException("Library item already exists!");
        }

        libraryRepository.save(UserLibrary.builder()
                .user(user)
                .title(dto.getTitle())
                .tmdbId(dto.getTmdbId())
                .mediaType(dto.getMediaType())
                .posterPath(dto.getPosterPath())
                .addedAt(LocalDateTime.now())  // always set by server, not client
                .build());
    }

    @Transactional
    public void removeFromLibrary(String username, Integer tmdbId, MediaType mediaType) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found!"));

        if (!libraryRepository.existsByUserIdAndTmdbIdAndMediaType(
                user.getId(), tmdbId, mediaType)) {
            throw new ItemNotFoundException("Item not found in library!");
        }

        libraryRepository.deleteByUserIdAndTmdbIdAndMediaType(
                user.getId(), tmdbId, mediaType);
    }

    public PagedResponseDto<MediaCardDTO> getLibrary(String username, MediaType mediaType, int page, int pageSize) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found!"));

        Pageable pageable = PageRequest.of(page, pageSize);

        Page<UserLibrary> result = mediaType != null
                ? libraryRepository.findByUserIdAndMediaTypeOrderByAddedAtDesc(
                user.getId(), mediaType, pageable)
                : libraryRepository.findByUserIdOrderByAddedAtDesc(
                user.getId(), pageable);

        List<MediaCardDTO> items = result.getContent().stream()
                .map(item -> MediaCardDTO.builder()
                        .tmdbId(item.getTmdbId())
                        .title(item.getTitle())
                        .posterPath(item.getPosterPath())
                        .mediaType(item.getMediaType())
                        .build())
                .toList();

        return PagedResponseDto.<MediaCardDTO>builder()
                .results(items)
                .page(result.getNumber() + 1)
                .totalPages(result.getTotalPages())
                .build();
    }

    public boolean isInLibrary(String username, Integer tmdbId, MediaType mediaType) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found!"));
        return libraryRepository.existsByUserIdAndTmdbIdAndMediaType(
                user.getId(), tmdbId, mediaType);
    }

}
