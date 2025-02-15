package com.robotlab.Shortify.service;

import com.robotlab.Shortify.constants.Role;
import com.robotlab.Shortify.dto.AuthenticationRequest;
import com.robotlab.Shortify.dto.AuthenticationResponse;
import com.robotlab.Shortify.dto.RegistrationRequest;
import com.robotlab.Shortify.entity.User;
import com.robotlab.Shortify.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public ResponseEntity<AuthenticationResponse> register(RegistrationRequest request) {
        User user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.valueOf(request.getRole()))
                .build();
        try {
            userRepository.save(user);
            String jwtToken = jwtService.generateToken(user);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(AuthenticationResponse.builder()
                        .token(jwtToken)
                        .message("User registered successfully")
                        .build());
        }
        catch(Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(AuthenticationResponse.builder()
                        .message("User already exists")
                        .build());
        }
    }

    public ResponseEntity<AuthenticationResponse> authenticate(AuthenticationRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
            User user = userRepository.findByEmail(request.getEmail()).orElseThrow();
            String jwtToken = jwtService.generateToken(user);
            return ResponseEntity.status(HttpStatus.ACCEPTED)
                    .body(AuthenticationResponse.builder()
                            .token(jwtToken)
                            .message("User logged in successfully")
                            .build());
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(AuthenticationResponse.builder()
                            .message("Invalid credentials")
                            .build());
        }
    }
}
