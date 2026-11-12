package com.tsoft.interview.pet_api.service;

import com.tsoft.interview.pet_api.model.dto.request.PostPetRequest;
import com.tsoft.interview.pet_api.model.dto.response.PostPetResponse;
import com.tsoft.interview.pet_api.model.dto.response.GetPetResponse;

public interface PetService {

    GetPetResponse getPet(Long idPet);

    PostPetResponse addPet(PostPetRequest request);
}
