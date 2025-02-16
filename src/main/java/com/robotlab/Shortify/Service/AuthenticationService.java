package com.robotlab.Shortify.Service;

import com.robotlab.Shortify.Dto.AuthenticationRequest;
import com.robotlab.Shortify.Dto.AuthenticationResponse;
import com.robotlab.Shortify.Dto.RegistrationRequest;
import org.springframework.http.ResponseEntity;

public interface AuthenticationService {

    ResponseEntity<AuthenticationResponse> register(RegistrationRequest request);

    ResponseEntity<AuthenticationResponse> authenticate(AuthenticationRequest request);

}
