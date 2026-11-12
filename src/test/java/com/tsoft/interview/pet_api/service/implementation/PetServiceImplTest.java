package com.tsoft.interview.pet_api.service.implementation;

import com.tsoft.interview.pet_api.client.PetStoreClient;
import com.tsoft.interview.pet_api.exception.PetStoreNotFoundException;
import com.tsoft.interview.pet_api.model.dto.request.PostPetRequest;
import com.tsoft.interview.pet_api.model.dto.response.GetPetResponse;
import com.tsoft.interview.pet_api.model.dto.response.PostPetResponse;
import com.tsoft.interview.pet_api.model.petstore.request.ExternalPetRequest;
import com.tsoft.interview.pet_api.model.petstore.response.ExternalPetResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PetServiceImplTest {

    @Mock
    private PetStoreClient petStoreClient;

    @InjectMocks
    private PetServiceImpl petService;

    private static ExternalPetResponse externalPet(Long id, String name, String status) {
        ExternalPetResponse response = new ExternalPetResponse();
        response.setId(id);
        response.setName(name);
        response.setStatus(status);
        return response;
    }

    @Test
    void getPet_debeMapearRespuestaExterna() {
        when(petStoreClient.getPetById(10L)).thenReturn(externalPet(10L, "Firulais", "available"));

        GetPetResponse response = petService.getPet(10L);

        assertEquals(10L, response.getId());
        assertEquals("Firulais", response.getName());
        assertEquals("available", response.getStatus());
        verify(petStoreClient).getPetById(10L);
    }

    @Test
    void getPet_cuandoNoExiste_debePropagarExcepcion() {
        when(petStoreClient.getPetById(99L))
                .thenThrow(new PetStoreNotFoundException("No se encontro el pet con el id 99"));

        PetStoreNotFoundException ex = assertThrows(PetStoreNotFoundException.class, () -> petService.getPet(99L));

        assertEquals("No se encontro el pet con el id 99", ex.getMessage());
    }

    @Test
    void addPet_debeEnviarRequestYMapearRespuesta() {
        when(petStoreClient.createPet(any(ExternalPetRequest.class)))
                .thenReturn(externalPet(5L, "Michi", "pending"));

        PostPetResponse response = petService.addPet(new PostPetRequest(5L, "pending", "Michi"));

        ArgumentCaptor<ExternalPetRequest> captor = ArgumentCaptor.forClass(ExternalPetRequest.class);
        verify(petStoreClient).createPet(captor.capture());
        ExternalPetRequest sent = captor.getValue();
        assertEquals(5L, sent.getId());
        assertEquals("Michi", sent.getName());
        assertEquals("pending", sent.getStatus());
        assertNull(sent.getCategory());
        assertTrue(sent.getPhotoUrls().isEmpty());
        assertTrue(sent.getTags().isEmpty());

        assertEquals("Michi", response.getName());
        assertEquals("pending", response.getStatus());
        assertDoesNotThrow(() -> UUID.fromString(response.getTransactionId()));
        assertDoesNotThrow(() -> LocalDateTime.parse(response.getDateCreated()));
    }
}
