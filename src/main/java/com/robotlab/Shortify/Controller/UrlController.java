package com.robotlab.Shortify.Controller;

import com.robotlab.Shortify.Dto.UrlDto;
import com.robotlab.Shortify.Dto.UrlResponse;
import com.robotlab.Shortify.Service.UrlShortenerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/shorten")
@RequiredArgsConstructor
public class UrlController {

    private final UrlShortenerService urlShortenerService;

    @PostMapping
    public ResponseEntity<?> shorten(@RequestBody UrlDto urlDto) {
        try {
            UrlResponse urlResponse = urlShortenerService.shorten(urlDto);
            return ResponseEntity
                    .status(200)
                    .body(urlResponse);
        } catch (RuntimeException e) {
            return ResponseEntity
                    .internalServerError()
                    .body(e.getMessage());
        }
    }

}
