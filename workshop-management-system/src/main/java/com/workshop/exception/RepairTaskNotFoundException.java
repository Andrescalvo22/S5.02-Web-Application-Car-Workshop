package com.workshop.exception;

public class RepairTaskNotFoundException extends RuntimeException {
    public RepairTaskNotFoundException(Long id) {
        super("Repair task with id " + id + " not found");
    }
}

