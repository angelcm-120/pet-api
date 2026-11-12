package com.tsoft.interview.pet_api.exception;

public class PetStoreNotFoundException extends RuntimeException {
    public PetStoreNotFoundException(String message) {
        super(message);
    }
}