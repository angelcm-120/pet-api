package com.tsoft.interview.pet_api.model.petstore.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ExternalPetResponse {

    private Long id;
    private String name;
    private String status;
}
