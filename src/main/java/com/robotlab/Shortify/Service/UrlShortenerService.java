package com.robotlab.Shortify.Service;

import com.robotlab.Shortify.Dto.UrlDto;
import com.robotlab.Shortify.Dto.UrlResponse;

public interface UrlShortenerService {

    UrlResponse shorten(UrlDto urlDto) throws RuntimeException;

}
