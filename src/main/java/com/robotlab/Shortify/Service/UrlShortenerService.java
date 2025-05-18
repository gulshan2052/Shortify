package com.robotlab.Shortify.Service;

import com.robotlab.Shortify.Dto.UrlDto;
import com.robotlab.Shortify.Dto.UrlResponse;
import jakarta.servlet.http.HttpServletRequest;

public interface UrlShortenerService {

    UrlResponse shorten(UrlDto urlDto, HttpServletRequest request) throws RuntimeException;

}
