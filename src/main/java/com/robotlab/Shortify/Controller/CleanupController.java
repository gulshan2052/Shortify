package com.robotlab.Shortify.Controller;

import com.robotlab.Shortify.Service.CleanupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping(("/api/v1/cleanup"))
public class CleanupController {

    private final CleanupService cleanupService;

    @PostMapping
    public ResponseEntity<?> cleanUp() {
        cleanupService.cleanUp();
        return ResponseEntity.ok("Cleanup started");
    }

}
