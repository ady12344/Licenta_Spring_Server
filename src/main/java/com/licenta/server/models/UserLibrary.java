package com.licenta.server.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "user_library" , indexes = {
        @Index(columnList = "user_id, tmdb_id, media_type")
})
public class UserLibrary {
    @Id @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private Integer tmdbId;
    private String mediaType;
    private String title;
    private String posterPath;

    private LocalDateTime addedAt;

}
