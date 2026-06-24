package com.example.core_banking.exception;

public class SameAccountTransferNotAllowedException extends RuntimeException {
    public SameAccountTransferNotAllowedException(String message) {
        super(message);
    }
}
