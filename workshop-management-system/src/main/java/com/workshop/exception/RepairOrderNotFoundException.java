package com.workshop.exception;

public class RepairOrderNotFoundException extends RuntimeException {
    public RepairOrderNotFoundException(Long id) {
        super("Repair order with id " + id + " not found");
    }
}

