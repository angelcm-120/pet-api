package com.tsoft.interview.pet_api.client;

import com.tsoft.interview.pet_api.exception.PetStoreException;
import com.tsoft.interview.pet_api.exception.PetStoreNotFoundException;
import com.tsoft.interview.pet_api.model.petstore.request.ExternalPetRequest;
import com.tsoft.interview.pet_api.model.petstore.response.ExternalPetResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@AllArgsConstructor
public class PetStoreClientImpl implements PetStoreClient {

    private final RestClient restClient;

    @Override
    public ExternalPetResponse getPetById(Long petId) {
        return restClient.get()
                .uri("/pet/{petId}", petId)
                .retrieve()
                .onStatus(status -> status.value() == 404, (req, res) -> {
                    throw new PetStoreNotFoundException("No se encontro el pet con el id " + petId);
                })
                .body(ExternalPetResponse.class);
    }

    @Override
    public ExternalPetResponse createPet(ExternalPetRequest request) {
        return restClient.post()
                .uri("/pet")
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .retrieve()
                .onStatus(status -> status.value() != 200, (req, res) -> {
                    throw new PetStoreException("Error al crear el pet");
                })
                .body(ExternalPetResponse.class);
    }
}
