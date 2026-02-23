package com.soen345.ticketreservation.exception;

public class EventFullException extends RuntimeException {

    public EventFullException(String message) {
        super(message);
    }
}
