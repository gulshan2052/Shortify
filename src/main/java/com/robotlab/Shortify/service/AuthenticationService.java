package com.robotlab.Shortify.service;

import com.robotlab.Shortify.dto.AuthenticationRequest;
import com.robotlab.Shortify.dto.AuthenticationResponse;
import com.robotlab.Shortify.dto.RegistrationRequest;
import org.springframework.http.ResponseEntity;

public interface AuthenticationService {

    ResponseEntity<AuthenticationResponse> register(RegistrationRequest request);

    ResponseEntity<AuthenticationResponse> authenticate(AuthenticationRequest request);

}
