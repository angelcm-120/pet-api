package com.tsoft.interview.pet_api.service.implementation;

import com.tsoft.interview.pet_api.client.PetStoreClient;
import com.tsoft.interview.pet_api.model.dto.request.PostPetRequest;
import com.tsoft.interview.pet_api.model.dto.response.PostPetResponse;
import com.tsoft.interview.pet_api.model.dto.response.GetPetResponse;
import com.tsoft.interview.pet_api.model.petstore.request.ExternalPetRequest;
import com.tsoft.interview.pet_api.model.petstore.response.ExternalPetResponse;
import com.tsoft.interview.pet_api.service.PetService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Slf4j
@AllArgsConstructor
public class PetServiceImpl implements PetService {

    private final PetStoreClient petStoreClient;

    @Override
    public GetPetResponse getPet(Long idPet) {
        ExternalPetResponse externalPet = petStoreClient.getPetById(idPet);

        log.info("Pet obtenida de Petstore: {}", externalPet);

        return GetPetResponse.builder()
                .id(externalPet.getId())
                .name(externalPet.getName())
                .status(externalPet.getStatus())
                .build();
    }

    @Override
    public PostPetResponse addPet(PostPetRequest request) {
        ExternalPetRequest externalRequest = ExternalPetRequest.builder()
                .id(request.id())
                .name(request.name())
                .status(request.status())
                .build();

        ExternalPetResponse externalPet = petStoreClient.createPet(externalRequest);

        log.info("Pet creada en Petstore: {}", externalPet);

        return PostPetResponse.builder()
                .transactionId(UUID.randomUUID().toString())
                .dateCreated(LocalDateTime.now().toString())
                .status(externalPet.getStatus())
                .name(externalPet.getName())
                .build();
    }
}
