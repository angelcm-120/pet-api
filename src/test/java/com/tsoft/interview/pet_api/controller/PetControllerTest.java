package com.tsoft.interview.pet_api.controller;

import com.tsoft.interview.pet_api.exception.GlobalExceptionHandler;
import com.tsoft.interview.pet_api.exception.PetStoreException;
import com.tsoft.interview.pet_api.exception.PetStoreNotFoundException;
import com.tsoft.interview.pet_api.model.dto.request.PostPetRequest;
import com.tsoft.interview.pet_api.model.dto.response.GetPetResponse;
import com.tsoft.interview.pet_api.model.dto.response.PostPetResponse;
import com.tsoft.interview.pet_api.service.PetService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class PetControllerTest {

    private static final String PET_JSON = "{\"id\":5,\"status\":\"pending\",\"name\":\"Michi\"}";

    @Mock
    private PetService petService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new PetController(petService))
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void getPet_debeRetornar200ConElPet() throws Exception {
        when(petService.getPet(1L)).thenReturn(new GetPetResponse(1L, "Firulais", "available"));

        mockMvc.perform(get("/api/pet/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Firulais"))
                .andExpect(jsonPath("$.status").value("available"));
    }

    @Test
    void getPet_cuandoNoExiste_debeRetornar404() throws Exception {
        when(petService.getPet(99L))
                .thenThrow(new PetStoreNotFoundException("No se encontro el pet con el id 99"));

        mockMvc.perform(get("/api/pet/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("No se encontro el pet con el id 99"));
    }

    @Test
    void addPet_debeRetornar201ConLaRespuesta() throws Exception {
        PostPetRequest request = new PostPetRequest(5L, "pending", "Michi");
        when(petService.addPet(request))
                .thenReturn(new PostPetResponse("tx-123", "2026-11-11T10:00", "pending", "Michi"));

        mockMvc.perform(post("/api/pet").contentType(MediaType.APPLICATION_JSON).content(PET_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.transactionId").value("tx-123"))
                .andExpect(jsonPath("$.dateCreated").value("2026-11-11T10:00"))
                .andExpect(jsonPath("$.status").value("pending"))
                .andExpect(jsonPath("$.name").value("Michi"));

        verify(petService).addPet(request);
    }

    @Test
    void addPet_cuandoFallaElServicio_debeRetornar500() throws Exception {
        PostPetRequest request = new PostPetRequest(5L, "pending", "Michi");
        when(petService.addPet(request)).thenThrow(new PetStoreException("Error al crear el pet"));

        mockMvc.perform(post("/api/pet").contentType(MediaType.APPLICATION_JSON).content(PET_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error").value("Internal Server Error"))
                .andExpect(jsonPath("$.message").value("Error al crear el pet"));
    }
}
