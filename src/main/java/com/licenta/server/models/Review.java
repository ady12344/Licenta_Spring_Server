package com.licenta.server.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "reviews", indexes = {
        @Index(columnList = "tmdb_id, media_type")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Review {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private Integer tmdbId;

    @Enumerated(EnumType.STRING)
    private MediaType mediaType;

    private Boolean liked; // true = liked, false = disliked

    private String content; // optional text

    private LocalDateTime createdAt;
}