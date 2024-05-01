package com.andersen.lab.integration.controller;

import com.andersen.lab.integration.IntegrationTestBase;
import com.andersen.lab.model.AuthenticationRequest;
import com.andersen.lab.model.RegisterRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RequiredArgsConstructor
class AuthenticationControllerTest extends IntegrationTestBase {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private AuthenticationRequest authenticationRequest;
    private RegisterRequest registerRequest;

    @BeforeEach
    void setMockUp() {
        authenticationRequest = AuthenticationRequest.builder()
                .email("test@test.com")
                .password("test")
                .build();
        registerRequest = RegisterRequest.builder()
                .firstName("Test")
                .lastName("Testov")
                .password("MySecretPass")
                .email("test1@test1.com")
                .build();
    }

    @Test
    void whenRegister_thenSuccess() throws Exception {
        mockMvc.perform(post("/api/v1/auth/register")
                        .content(objectMapper.writeValueAsString(registerRequest))
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void whenAuthenticate_thenSuccess() throws Exception {
        mockMvc.perform(post("/api/v1/auth/authenticate")
                        .content(objectMapper.writeValueAsString(authenticationRequest))
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}