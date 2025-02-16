package com.robotlab.Shortify.Service;

import com.robotlab.Shortify.Entity.UrlDetail;
import com.robotlab.Shortify.Repository.UrlDetailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class HomeServiceImpl implements HomeService {

    private final UrlDetailRepository urlDetailRepository;

    public String getLongUrl(String id) {
        Optional<UrlDetail> urlDetailOptional = urlDetailRepository.findById(id);
        return urlDetailOptional.map(UrlDetail::getLongUrl).orElse(null);
    }

}
