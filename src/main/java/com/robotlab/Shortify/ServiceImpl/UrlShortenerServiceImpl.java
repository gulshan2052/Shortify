package com.robotlab.Shortify.ServiceImpl;

import com.robotlab.Shortify.Dto.UrlDto;
import com.robotlab.Shortify.Dto.UrlResponse;
import com.robotlab.Shortify.Entity.UrlDetail;
import com.robotlab.Shortify.Entity.User;
import com.robotlab.Shortify.Repository.UrlDetailRepository;
import com.robotlab.Shortify.Service.LengthConfigService;
import com.robotlab.Shortify.Service.UrlShortenerService;
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
    private final LengthConfigService lengthConfigService;

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
                    .expirationDate(now.plusMinutes(urlDto.getTtlMinutes()))
                    .user(user)
                    .build();
            urlDetailRepository.save(urlDetail);

            return UrlResponse.builder()
                    .status(1)
                    .longUrl(urlDto.getLongUrl())
                    .shortUrl("http://localhost:8080/" + alias) // TODO - hostname
                    .message("Link has been shortened")
                    .build();
        }
        // TODO - test this part
        return createShortUrl(urlDto, now, user);

    }

    public UrlResponse createShortUrl(UrlDto urlDto, OffsetDateTime now, User user) {
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
                        .shortUrl("http://localhost:8080/" + alias) // TODO - hostname
                        .message("Link has been shortened")
                        .build();
            }
        }

        throw new RuntimeException("Something went wrong");
    }

}
