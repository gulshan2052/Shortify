package com.robotlab.shortify.config;

import com.robotlab.shortify.service.RateLimitResult;
import com.robotlab.shortify.service.RateLimitingService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class RateLimitingFilter extends OncePerRequestFilter {

    private final RateLimitingService rateLimitingService;
    private final RateLimitProperties properties;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        if (request.getMethod().equals("OPTIONS") || response.isCommitted()) {
            filterChain.doFilter(request, response);
            return;
        }

        String identifier = resolveIdentifier(request);
        RateLimitResult result = rateLimitingService.checkRateLimit(identifier);

        response.setHeader("X-RateLimit-Limit", String.valueOf(properties.getCapacity()));
        response.setHeader("X-RateLimit-Remaining", String.valueOf(result.getRemainingTokens()));

        if (!result.isAllowed()) {
            int retryAfter = (int) Math.ceil((double) properties.getCapacity() / (double) properties.getRefillRate());
            response.setHeader("Retry-After", String.valueOf(retryAfter));
            response.setStatus(429);
            response.setContentType("application/json");
            response.getWriter().write(
                    "{\"error\":\"Too Many Requests\",\"retryAfterSeconds\":" + retryAfter + "}");
            return;
        }

        filterChain.doFilter(request, response);
    }

    private String resolveIdentifier(HttpServletRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()
                && !(auth instanceof AnonymousAuthenticationToken)) {
            return auth.getName();
        }
        return request.getRemoteAddr();
    }
}
