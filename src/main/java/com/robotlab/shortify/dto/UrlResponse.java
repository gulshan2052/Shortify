package com.robotlab.shortify.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UrlResponse {

    private Integer status;
    private String message;
    private String longUrl;
    private String alias;
    private String shortUrl;

}

/*

1) Good response
{
    'status': 1,
    'longUrl': 'https://www.google.com',
    'shortUrl': 'https://cutt.ly/08Z0kXL',
    'alias': '08Z0kXL',
    'message': 'Link has been shortened'
}

2) Bad url
{
    'status': 2,
    'longUrl': 'https://www.google.com',
    'message': 'Invalid URL'
}

3) Alias already taken
{
    'status': 3,
    'longUrl': 'https://www.google.com',
    'message': 'Alias already taken'
}

4) Invalid alias
{
    'status': 4,
    'longUrl': 'https://www.google.com',
    'message': 'Alias contains invalid characters'
}

5) Something went wrong
{

}

*/