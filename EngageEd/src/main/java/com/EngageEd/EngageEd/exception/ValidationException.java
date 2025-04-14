package com.EngageEd.EngageEd.exception;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when data validation fails.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ValidationException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    
    private final Map<String, String> errors;
    
    public ValidationException(String message) {
        super(message);
        this.errors = null;
    }
    
    public ValidationException(String message, Map<String, String> errors) {
        super(message);
        this.errors = errors;
    }
    
    public ValidationException(String message, Throwable cause) {
        super(message, cause);
        this.errors = null;
    }
    
    public Map<String, String> getErrors() {
        return errors;
    }
}