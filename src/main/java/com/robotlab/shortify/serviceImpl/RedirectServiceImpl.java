package com.robotlab.shortify.serviceImpl;

import com.robotlab.shortify.entity.UrlDetail;
import com.robotlab.shortify.repository.UrlDetailRepository;
import com.robotlab.shortify.service.RedirectService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class RedirectServiceImpl implements RedirectService {

    private final UrlDetailRepository urlDetailRepository;

    @Cacheable(value = "urls", key = "#id", unless = "#result == null")
    public String getLongUrl(String id) {
        log.info("Cache MISS - Fetching from DB for ID: {}", id);
        Optional<UrlDetail> urlDetailOptional = urlDetailRepository.findById(id);
        if(urlDetailOptional.isEmpty()) {
            return null;
        }
        UrlDetail urlDetail = urlDetailOptional.get();
        OffsetDateTime now = OffsetDateTime.now();
        if(urlDetail.getExpirationDate().isAfter(now)) {
            return urlDetail.getLongUrl();
        }
        return null;
    }

}
