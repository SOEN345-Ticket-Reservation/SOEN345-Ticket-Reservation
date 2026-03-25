package com.soen345.ticketreservation.exception;

public class EmailConfirmationException extends RuntimeException {

    public EmailConfirmationException(String message, Throwable cause) {
        super(message, cause);
    }
}
