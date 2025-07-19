package com.robotlab.shortify.controller;

import com.robotlab.shortify.dto.UrlDto;
import com.robotlab.shortify.dto.UrlResponse;
import com.robotlab.shortify.service.UrlShortenerService;
import jakarta.servlet.http.HttpServletRequest;
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
    public ResponseEntity<?> shorten(HttpServletRequest request, @RequestBody UrlDto urlDto) {
        try {
            UrlResponse urlResponse = urlShortenerService.shorten(urlDto, request);
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
