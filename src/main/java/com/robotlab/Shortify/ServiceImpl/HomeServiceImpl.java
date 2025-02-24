package com.robotlab.Shortify.ServiceImpl;

import com.robotlab.Shortify.Entity.UrlDetail;
import com.robotlab.Shortify.Repository.UrlDetailRepository;
import com.robotlab.Shortify.Service.HomeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class HomeServiceImpl implements HomeService {

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
