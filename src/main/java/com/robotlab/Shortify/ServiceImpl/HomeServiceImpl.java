package com.robotlab.Shortify.ServiceImpl;

import com.robotlab.Shortify.Entity.UrlDetail;
import com.robotlab.Shortify.Repository.UrlDetailRepository;
import com.robotlab.Shortify.Service.HomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class HomeServiceImpl implements HomeService {

    private final UrlDetailRepository urlDetailRepository;

    public String getLongUrl(String id) {
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
