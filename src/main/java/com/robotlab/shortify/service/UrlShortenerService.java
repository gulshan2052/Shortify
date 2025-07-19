package com.robotlab.shortify.service;

import com.robotlab.shortify.dto.UrlDto;
import com.robotlab.shortify.dto.UrlResponse;
import jakarta.servlet.http.HttpServletRequest;

public interface UrlShortenerService {

    UrlResponse shorten(UrlDto urlDto, HttpServletRequest request) throws RuntimeException;

}
