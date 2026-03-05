package com.licenta.server.services;

import com.licenta.server.dto.LibraryItemDTO;
import com.licenta.server.dto.PagedResponseDto;
import com.licenta.server.exception.ItemNotFoundException;
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

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserLibraryService {
    private final UserLibraryRepository libraryRepository;
    private final UserRepository userRepository;
    @Transactional
    public void addToLibrary(String username, LibraryItemDTO libraryItemDTO) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found!"));
        if(libraryRepository.existsByUserIdAndTmdbIdAndMediaType(user.getId() , libraryItemDTO.getTmdbId(),  libraryItemDTO.getMediaType())){
            throw new RuntimeException("Library item already exists!");
        }
        UserLibrary userLibrary = UserLibrary.builder()
                .user(user)
                .title(libraryItemDTO.getTitle())
                .tmdbId(libraryItemDTO.getTmdbId())
                .mediaType(libraryItemDTO.getMediaType())
                .posterPath(libraryItemDTO.getPosterPath())
                .addedAt(libraryItemDTO.getCreatedAt())
                .build();
        libraryRepository.save(userLibrary);
    }
    @Transactional
    public void removeFromLibrary(String username, Integer tndbId , String mediaType) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found!"));
        if(!libraryRepository.existsByUserIdAndTmdbIdAndMediaType(user.getId(), tndbId, mediaType)){
            throw new ItemNotFoundException("Item not found in library!");
        }
        libraryRepository.deleteByUserIdAndTmdbIdAndMediaType(user.getId(), tndbId, mediaType);
    }

    public PagedResponseDto<LibraryItemDTO> getLibrary(String username, String mediaType, int page, int pageSize) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found!"));
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<UserLibrary> result = mediaType != null
                ? libraryRepository.findByUserIdAndMediaTypeOrderByAddedAtDesc(
                user.getId(), mediaType, pageable)
                : libraryRepository.findByUserIdOrderByAddedAtDesc(
                user.getId(), pageable);

        List<LibraryItemDTO> items = result.getContent().stream().map(item ->
                LibraryItemDTO.builder()
                        .tmdbId(item.getTmdbId())
                        .title(item.getTitle())
                        .mediaType(item.getMediaType())
                        .posterPath(item.getPosterPath())
                        .createdAt(item.getAddedAt())
                        .build()
                ).toList();

        return PagedResponseDto.<LibraryItemDTO>builder()
                .results(items)
                .page(page)
                .totalPages(result.getTotalPages())
                .build();

    }

    public boolean isInLibrary(String username, Integer tndbId , String mediaType) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found!"));
        return libraryRepository.existsByUserIdAndTmdbIdAndMediaType(user.getId(), tndbId, mediaType);
    }

}
