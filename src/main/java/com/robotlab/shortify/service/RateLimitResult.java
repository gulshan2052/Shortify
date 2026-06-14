package com.robotlab.shortify.service;

import lombok.Getter;

@Getter
public class RateLimitResult {

    private final boolean allowed;
    private final int remainingTokens;

    public RateLimitResult(boolean allowed, int remainingTokens) {
        this.allowed = allowed;
        this.remainingTokens = remainingTokens;
    }

}
