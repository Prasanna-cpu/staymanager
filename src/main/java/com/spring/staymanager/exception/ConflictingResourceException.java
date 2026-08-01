package com.spring.staymanager.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class ConflictingResourceException extends RuntimeException {
    public ConflictingResourceException(String message) {
        super(message);
    }
}
