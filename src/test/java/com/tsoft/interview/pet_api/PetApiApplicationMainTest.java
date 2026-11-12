package com.tsoft.interview.pet_api;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.boot.SpringApplication;

import static org.mockito.Mockito.mockStatic;

class PetApiApplicationMainTest {

    @Test
    void main_debeIniciarSpringApplication() {
        String[] args = {"--server.port=0"};
        try (MockedStatic<SpringApplication> springApplication = mockStatic(SpringApplication.class)) {
            PetApiApplication.main(args);

            springApplication.verify(() -> SpringApplication.run(PetApiApplication.class, args));
        }
    }
}
