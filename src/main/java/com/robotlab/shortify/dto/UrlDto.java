package com.robotlab.shortify.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UrlDto {

    private String alias;
    private String longUrl;
    private Integer ttlMinutes;

}
