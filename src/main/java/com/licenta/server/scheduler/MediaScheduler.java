package com.licenta.server.scheduler;

import com.licenta.server.models.Media;
import com.licenta.server.repository.MediaRepository;
import com.licenta.server.services.MediaService;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

import static reactor.netty.http.HttpConnectionLiveness.log;

@Component
@RequiredArgsConstructor
public class MediaScheduler {
    private final MediaRepository mediaRepository;
    private final MediaService mediaService;

    //@Scheduled(cron = "0 0 3 * * *") // 3 AM
    @Scheduled(cron = "${media.refresh.cron}")
    @Transactional
    public void refreshStaleLibraryMediaNightly() {
        log.info("MediaScheduler triggered at {}", Instant.now());
        List<Media> stale = mediaRepository.findStaleInLibraries(
                Instant.now().minus(Duration.ofDays(7)),
                PageRequest.of(0, 200)
        );

        for (Media m : stale) {
            mediaService.upsertAndSyncMedia(m.getMediaType(), m.getTmdbId());
        }
    }

}
