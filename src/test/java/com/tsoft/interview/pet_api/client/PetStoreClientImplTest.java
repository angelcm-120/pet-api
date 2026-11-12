package com.tsoft.interview.pet_api.client;

import com.tsoft.interview.pet_api.exception.PetStoreException;
import com.tsoft.interview.pet_api.exception.PetStoreNotFoundException;
import com.tsoft.interview.pet_api.model.petstore.dto.Category;
import com.tsoft.interview.pet_api.model.petstore.dto.Tag;
import com.tsoft.interview.pet_api.model.petstore.request.ExternalPetRequest;
import com.tsoft.interview.pet_api.model.petstore.response.ExternalPetResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClient;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withServerError;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

class PetStoreClientImplTest {

    private static final String BASE_URL = "https://petstore.test/v2";

    private MockRestServiceServer server;
    private PetStoreClientImpl client;

    @BeforeEach
    void setUp() {
        RestClient.Builder builder = RestClient.builder().baseUrl(BASE_URL);
        server = MockRestServiceServer.bindTo(builder).build();
        client = new PetStoreClientImpl(builder.build());
    }

    @Test
    void getPetById_debeRetornarElPet() {
        server.expect(requestTo(BASE_URL + "/pet/1"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess("{\"id\":1,\"name\":\"Firulais\",\"status\":\"available\",\"photoUrls\":[]}",
                        MediaType.APPLICATION_JSON));

        ExternalPetResponse response = client.getPetById(1L);

        assertEquals(1L, response.getId());
        assertEquals("Firulais", response.getName());
        assertEquals("available", response.getStatus());
        server.verify();
    }

    @Test
    void getPetById_cuandoRetorna404_debeLanzarNotFound() {
        server.expect(requestTo(BASE_URL + "/pet/99"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.NOT_FOUND));

        PetStoreNotFoundException ex = assertThrows(PetStoreNotFoundException.class, () -> client.getPetById(99L));

        assertEquals("No se encontro el pet con el id 99", ex.getMessage());
        server.verify();
    }

    @Test
    void getPetById_cuandoRetornaOtroError_debeLanzarExcepcionPorDefecto() {
        server.expect(requestTo(BASE_URL + "/pet/2"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withServerError());

        assertThrows(HttpServerErrorException.class, () -> client.getPetById(2L));
        server.verify();
    }

    @Test
    void createPet_debeEnviarElPetYRetornarRespuesta() {
        ExternalPetRequest request = ExternalPetRequest.builder()
                .id(5L)
                .name("Michi")
                .status("pending")
                .category(new Category(1L, "Gatos"))
                .photoUrls(List.of("http://foto"))
                .tags(List.of(new Tag(2L, "tierno")))
                .build();

        server.expect(requestTo(BASE_URL + "/pet"))
                .andExpect(method(HttpMethod.POST))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(5))
                .andExpect(jsonPath("$.name").value("Michi"))
                .andExpect(jsonPath("$.status").value("pending"))
                .andExpect(jsonPath("$.category.name").value("Gatos"))
                .andExpect(jsonPath("$.photoUrls[0]").value("http://foto"))
                .andExpect(jsonPath("$.tags[0].name").value("tierno"))
                .andRespond(withSuccess("{\"id\":5,\"name\":\"Michi\",\"status\":\"pending\"}",
                        MediaType.APPLICATION_JSON));

        ExternalPetResponse response = client.createPet(request);

        assertEquals(5L, response.getId());
        assertEquals("Michi", response.getName());
        assertEquals("pending", response.getStatus());
        server.verify();
    }

    @Test
    void createPet_cuandoNoRetorna200_debeLanzarPetStoreException() {
        server.expect(requestTo(BASE_URL + "/pet"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withStatus(HttpStatus.BAD_REQUEST));

        ExternalPetRequest request = ExternalPetRequest.builder().name("Michi").build();

        PetStoreException ex = assertThrows(PetStoreException.class, () -> client.createPet(request));

        assertEquals("Error al crear el pet", ex.getMessage());
        server.verify();
    }
}
