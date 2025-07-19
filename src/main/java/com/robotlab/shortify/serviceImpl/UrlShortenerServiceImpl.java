package com.robotlab.shortify.serviceImpl;

import com.robotlab.shortify.dto.UrlDto;
import com.robotlab.shortify.dto.UrlResponse;
import com.robotlab.shortify.entity.UrlDetail;
import com.robotlab.shortify.entity.User;
import com.robotlab.shortify.repository.UrlDetailRepository;
import com.robotlab.shortify.service.LengthConfigService;
import com.robotlab.shortify.service.UrlShortenerService;
import com.robotlab.shortify.utils.UserContextUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.Optional;

import static com.robotlab.shortify.utils.UrlUtils.*;

@Service
@RequiredArgsConstructor
public class UrlShortenerServiceImpl implements UrlShortenerService {

    private final UrlDetailRepository urlDetailRepository;
    private final LengthConfigService lengthConfigService;

    @Override
    public UrlResponse shorten(UrlDto urlDto, HttpServletRequest request) throws RuntimeException {

        String baseUrl = getBaseUrl(request);
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
                    .expirationDate(now.plusMinutes(urlDto.getTtlMinutes()))
                    .user(user)
                    .build();
            urlDetailRepository.save(urlDetail);

            return UrlResponse.builder()
                    .status(1)
                    .longUrl(urlDto.getLongUrl())
                    .shortUrl(baseUrl + "/" + alias)
                    .alias(alias)
                    .message("Link has been shortened")
                    .build();
        }
        return createShortUrl(urlDto, now, user, baseUrl);

    }

    public UrlResponse createShortUrl(UrlDto urlDto, OffsetDateTime now, User user, String baseUrl) {
        int trials = 5;
        int doIncrement = 3;
        String alias;
        for(int i = 0; i < trials; i++){
            if(i == doIncrement){
                lengthConfigService.incrementAliasLength();
            }
            alias = generateRandomAlias(lengthConfigService.getLength());
            Optional<UrlDetail> urlDetailOptional = urlDetailRepository.findById(alias);
            if(urlDetailOptional.isEmpty()) {
                UrlDetail urlDetail = UrlDetail.builder()
                        .id(alias)
                        .longUrl(urlDto.getLongUrl())
                        .expirationDate(now.plusMinutes(urlDto.getTtlMinutes()))
                        .user(user)
                        .build();
                urlDetailRepository.save(urlDetail);

                return UrlResponse.builder()
                        .status(1)
                        .longUrl(urlDto.getLongUrl())
                        .shortUrl(baseUrl + "/" + alias)
                        .alias(alias)
                        .message("Link has been shortened")
                        .build();
            }
        }

        throw new RuntimeException("Something went wrong");
    }

    private String getBaseUrl(HttpServletRequest request) {
        String scheme = request.getScheme();
        String serverName = request.getServerName();
        int serverPort = request.getServerPort();

        boolean isDefaultPort = ("http".equals(scheme) && serverPort == 80) ||
                ("https".equals(scheme) && serverPort == 443);

        if (isDefaultPort) {
            return scheme + "://" + serverName;
        } else {
            return scheme + "://" + serverName + ":" + serverPort;
        }
    }

}
