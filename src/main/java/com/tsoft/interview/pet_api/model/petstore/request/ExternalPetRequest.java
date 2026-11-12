package com.tsoft.interview.pet_api.model.petstore.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tsoft.interview.pet_api.model.petstore.dto.Category;
import com.tsoft.interview.pet_api.model.petstore.dto.Tag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExternalPetRequest {

    private Long id;
    private Category category;
    private String name;

    @Builder.Default
    private List<String> photoUrls = new ArrayList<>();

    @Builder.Default
    private List<Tag> tags = new ArrayList<>();

    private String status;
}
