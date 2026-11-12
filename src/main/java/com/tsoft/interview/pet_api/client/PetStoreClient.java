package com.tsoft.interview.pet_api.client;

import com.tsoft.interview.pet_api.model.petstore.request.ExternalPetRequest;
import com.tsoft.interview.pet_api.model.petstore.response.ExternalPetResponse;

public interface PetStoreClient {

    ExternalPetResponse getPetById(Long petId);

    ExternalPetResponse createPet(ExternalPetRequest request);
}
