package com.robotlab.Shortify.Controller;

import com.robotlab.Shortify.Service.RedirectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;


@RequiredArgsConstructor
@RestController
public class RedirectController {

    private final RedirectService redirectService;

    @GetMapping("/{id}")
    public ResponseEntity<?> redirect(@PathVariable String id) {
        String longUrl = redirectService.getLongUrl(id);

        if (longUrl != null) {
            return ResponseEntity.status(HttpStatus.TEMPORARY_REDIRECT)
                    .header("Location", longUrl)
                    .build();
        } else {
            return ResponseEntity.status(404).body("Resource not found or has expired");
        }
    }

}
