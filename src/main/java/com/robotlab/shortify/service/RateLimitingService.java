package com.robotlab.shortify.service;

import com.robotlab.shortify.config.RateLimitProperties;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Service
public class RateLimitingService {

    private static final String KEY_PREFIX = "rate_limit:";

    private final StringRedisTemplate redisTemplate;
    private final RateLimitProperties properties;

    public RateLimitingService(StringRedisTemplate redisTemplate, RateLimitProperties properties) {
        this.redisTemplate = redisTemplate;
        this.properties = properties;
    }

    public RateLimitResult checkRateLimit(String identifier) {
        String key = KEY_PREFIX + identifier;
        Map<Object, Object> data = redisTemplate.opsForHash().entries(key);

        long now = System.currentTimeMillis() / 1000;
        int tokens;
        long ts;

        if (data.isEmpty()) {
            tokens = properties.getCapacity();
            ts = now;
        } else {
            tokens = Integer.parseInt((String) data.get("tokens"));
            ts = Long.parseLong((String) data.get("ts"));

            long elapsed = now - ts;
            if (elapsed >= properties.getRefillDurationSeconds()) {
                double refillPerSecond = (double) properties.getRefillRate() / properties.getRefillDurationSeconds();
                int refill = (int) (elapsed * refillPerSecond);
                if (refill > 0) {
                    tokens = Math.min(properties.getCapacity(), tokens + refill);
                    ts = now;
                }
            }
        }

        boolean allowed = tokens >= 1;
        if (allowed) {
            tokens--;
        }

        Map<String, String> updated = new HashMap<>();
        updated.put("tokens", String.valueOf(tokens));
        updated.put("ts", String.valueOf(ts));
        redisTemplate.opsForHash().putAll(key, updated);
        redisTemplate.expire(key, Duration.ofSeconds(properties.getCapacity() / properties.getRefillRate() * 2 + 1));

        return new RateLimitResult(allowed, tokens);
    }
}
