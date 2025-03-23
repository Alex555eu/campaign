package com.example.campaign.exception;

public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(String ex) {
        super(ex);
    }
}
