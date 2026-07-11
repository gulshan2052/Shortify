package com.robotlab.shortify.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Setter
@Getter
@ConfigurationProperties(prefix = "rate-limit")
public class RateLimitProperties {

    private int capacity;
    private int refillRate;
    private int refillDurationSeconds;

    public RateLimitProperties() {
    }

}
