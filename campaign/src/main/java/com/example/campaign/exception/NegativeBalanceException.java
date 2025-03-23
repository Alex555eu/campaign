package com.example.campaign.exception;

public class NegativeBalanceException extends RuntimeException {

    public NegativeBalanceException(String ex) {
        super(ex);
    }
}
