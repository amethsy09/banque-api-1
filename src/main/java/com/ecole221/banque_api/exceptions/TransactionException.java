package com.ecole221.banque_api.exceptions;

public class TransactionException extends RuntimeException {

    public TransactionException(String message) {
        super(message);
    }
}