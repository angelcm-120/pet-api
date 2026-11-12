package com.tsoft.interview.pet_api.config;

import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestClient;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class RestClientConfigTest {

    @Test
    void petstoreRestClient_debeCrearRestClient() {
        RestClient restClient = new RestClientConfig().petstoreRestClient("https://petstore.swagger.io/v2");

        assertNotNull(restClient);
    }
}
