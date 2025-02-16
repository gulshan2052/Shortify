package com.robotlab.Shortify.Service;

import com.robotlab.Shortify.Dto.UrlDto;
import com.robotlab.Shortify.Dto.UrlResponse;
import com.robotlab.Shortify.Entity.UrlDetail;
import com.robotlab.Shortify.Entity.User;
import com.robotlab.Shortify.Repository.UrlDetailRepository;
import com.robotlab.Shortify.Utils.UserContextUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.Optional;

import static com.robotlab.Shortify.Utils.UrlUtils.*;

@Service
@RequiredArgsConstructor
public class UrlShortenerServiceImpl implements UrlShortenerService {

    private final UrlDetailRepository urlDetailRepository;

    @Override
    public UrlResponse shorten(UrlDto urlDto) throws RuntimeException {

        User user = UserContextUtil.getCurrentUser();
        OffsetDateTime now = OffsetDateTime.now();

        if(!isUrlValid(urlDto.getLongUrl())) {
            return UrlResponse.builder()
                    .status(2)
                    .longUrl(urlDto.getLongUrl())
                    .message("Invalid URL")
                    .build();
        }

        String alias = urlDto.getAlias();
        if(alias != null && !alias.isEmpty()) {
            if(!isAlphaNumeric(alias)) {
                return UrlResponse.builder()
                        .status(4)
                        .longUrl(urlDto.getLongUrl())
                        .message("Alias contains invalid characters")
                        .build();
            }
            Optional<UrlDetail> urlDetailOptional = urlDetailRepository.findById(alias);
            if(urlDetailOptional.isPresent()) {
                return UrlResponse.builder()
                        .status(3)
                        .longUrl(urlDto.getLongUrl())
                        .message("Alias already taken")
                        .build();
            }
            UrlDetail urlDetail = UrlDetail.builder()
                    .id(alias)
                    .longUrl(urlDto.getLongUrl())
                    .ttlMinutes(urlDto.getTtlMinutes())
                    .createdDate(now)
                    .user(user)
                    .build();
            urlDetailRepository.save(urlDetail);

            return UrlResponse.builder()
                    .status(1)
                    .longUrl(urlDto.getLongUrl())
                    .shortUrl("http://localhost:8080/" + alias) // Todo - hostname
                    .message("Link has been shortened")
                    .build();
        }

        int length = 6;
        int trials = 3;
        for(int i = 0; i < trials; i++){
            alias = generateRandomAlias(length);
            Optional<UrlDetail> urlDetailOptional = urlDetailRepository.findById(alias);
            if(urlDetailOptional.isEmpty()) {
                UrlDetail urlDetail = UrlDetail.builder()
                        .id(alias)
                        .longUrl(urlDto.getLongUrl())
                        .ttlMinutes(urlDto.getTtlMinutes())
                        .createdDate(now)
                        .user(user)
                        .build();
                urlDetailRepository.save(urlDetail);

                return UrlResponse.builder()
                        .status(1)
                        .longUrl(urlDto.getLongUrl())
                        .shortUrl("http://localhost:8080/" + alias) // Todo - hostname
                        .message("Link has been shortened")
                        .build();
            }
        }

        throw new RuntimeException("Something went wrong");
    }
}
