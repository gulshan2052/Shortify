package com.robotlab.shortify.service;

import com.robotlab.shortify.dto.AuthenticationRequest;
import com.robotlab.shortify.dto.AuthenticationResponse;
import com.robotlab.shortify.dto.RegistrationRequest;
import org.springframework.http.ResponseEntity;

public interface AuthenticationService {

    ResponseEntity<AuthenticationResponse> register(RegistrationRequest request);

    ResponseEntity<AuthenticationResponse> authenticate(AuthenticationRequest request);

}
