package com.robotlab.Shortify.ServiceImpl;

import com.robotlab.Shortify.Repository.UrlDetailRepository;
import com.robotlab.Shortify.Service.CleanupService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;

@Slf4j
@RequiredArgsConstructor
@Service
public class CleanupServiceImpl implements CleanupService {

    private final UrlDetailRepository urlDetailRepository;

    @Async
    public void cleanUp() {
        OffsetDateTime now = OffsetDateTime.now();
        log.info("Cleanup started at {}", now);
        int deletedEntries = urlDetailRepository.deleteExpiredEntries(now);
        log.info("Cleanup removed {} expired records", deletedEntries);
    }

}
