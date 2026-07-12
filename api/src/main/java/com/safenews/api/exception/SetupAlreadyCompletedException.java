package com.safenews.api.exception;

public class SetupAlreadyCompletedException extends BusinessException {
    public SetupAlreadyCompletedException(String message) {
        super(message);
    }
}
