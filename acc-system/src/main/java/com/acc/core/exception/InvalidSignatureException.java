package com.acc.core.exception;

public class InvalidSignatureException extends Throwable {
    public InvalidSignatureException(String missingSignatureInRequest) {
        super(missingSignatureInRequest);
    }
}
