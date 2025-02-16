package com.robotlab.Shortify.Controller;

import com.robotlab.Shortify.Service.HomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;


@RequiredArgsConstructor
@RestController
public class HomeController {

    private final HomeService homeService;

    @GetMapping("/{id}")
    public ResponseEntity<?> home(@PathVariable String id) {
        String longUrl = homeService.getLongUrl(id);

        if (longUrl != null) {
            return ResponseEntity.status(HttpStatus.PERMANENT_REDIRECT)
                    .header("Location", longUrl)
                    .build();
        } else {
            return ResponseEntity.status(404).body("Resource not found or has expired");
        }
    }

}
