package com.tsoft.interview.pet_api.controller;

import com.tsoft.interview.pet_api.model.dto.request.PostPetRequest;
import com.tsoft.interview.pet_api.model.dto.response.PostPetResponse;
import com.tsoft.interview.pet_api.model.dto.response.GetPetResponse;
import com.tsoft.interview.pet_api.service.PetService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pet")
@AllArgsConstructor
public class PetController {

    private final PetService petService;

    @GetMapping("/{idPet}")
    public ResponseEntity<GetPetResponse> getPet(@PathVariable("idPet") Long idPet) {
        return ResponseEntity.ok(petService.getPet(idPet));
    }

    @PostMapping
    public ResponseEntity<PostPetResponse> addPet(@RequestBody PostPetRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(petService.addPet(request));
    }
}
