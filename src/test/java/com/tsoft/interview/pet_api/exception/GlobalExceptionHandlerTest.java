package com.tsoft.interview.pet_api.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void handleNotFound_debeRetornar404ConMensaje() {
        ResponseEntity<Object> response = handler.handleFeignException(
                new PetStoreNotFoundException("No se encontro el pet con el id 1"));

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(Map.of("message", "No se encontro el pet con el id 1"), response.getBody());
    }

    @Test
    void handleGeneralException_conMensaje_debeRetornar500ConMensaje() {
        ResponseEntity<Object> response = handler.handleGeneralException(new PetStoreException("Error al crear el pet"));

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(Map.of("error", "Internal Server Error", "message", "Error al crear el pet"), response.getBody());
    }

    @Test
    void handleGeneralException_sinMensaje_debeRetornarUnknownError() {
        ResponseEntity<Object> response = handler.handleGeneralException(new RuntimeException());

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(Map.of("error", "Internal Server Error", "message", "Unknown error"), response.getBody());
    }
}
