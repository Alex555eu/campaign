package com.example.campaign.dto;

public record RegisterRequest(
        String password,
        String firstName,
        String emailAddress
) {
}
