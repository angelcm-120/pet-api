package com.tsoft.interview.pet_api.model.dto.request;

public record PostPetRequest(
        Long id,
        String status,
        String name
) {
}
