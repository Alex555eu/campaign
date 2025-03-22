package com.example.campaign.dto;

public record AuthenticationRequest(
        String email,
        String password
) {
}
