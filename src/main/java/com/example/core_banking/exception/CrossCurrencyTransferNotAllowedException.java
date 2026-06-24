package com.example.core_banking.exception;

public class CrossCurrencyTransferNotAllowedException extends RuntimeException {
    public CrossCurrencyTransferNotAllowedException(String message) {
        super(message);
    }
}
