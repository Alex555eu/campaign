package com.example.campaign.controller;


import com.example.campaign.dto.AuthenticationRequest;
import com.example.campaign.dto.AuthenticationResponse;
import com.example.campaign.dto.PostRefreshTokenRequest;
import com.example.campaign.dto.RegisterRequest;
import com.example.campaign.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegisterRequest request
    ) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ) {
        return ResponseEntity.ok(authService.authenticate(request));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthenticationResponse> refreshToken(
            @RequestBody PostRefreshTokenRequest request
    ) {
        return authService.refreshToken(request);
    }
}
