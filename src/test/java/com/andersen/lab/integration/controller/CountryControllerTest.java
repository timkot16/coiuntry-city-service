package com.andersen.lab.integration.controller;

import com.andersen.lab.integration.IntegrationTestBase;
import com.andersen.lab.model.Country;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RequiredArgsConstructor
class CountryControllerTest extends IntegrationTestBase {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;

    @Test
    @SneakyThrows
    @WithMockUser
    void whenGetAll_thenSuccess() {
        mockMvc
                .perform(get("/api/v1/countries")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk());
    }

    @Test
    @SneakyThrows
    @WithAnonymousUser
    void whenGetAll_thenForbidden() {
        mockMvc
                .perform(get("/api/v1/countries")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isForbidden());
    }

    @Test
    @SneakyThrows
    @WithMockUser(roles = {"ADMIN", "EDITOR"})
    void whenSave_thenSuccess() {
        mockMvc
                .perform(post("/api/v1/countries")
                        .content(objectMapper.writeValueAsString(Country.builder()
                                .name("Armenia")
                                .build()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    @SneakyThrows
    @WithMockUser
    void whenSave_thenForbidden() {
        mockMvc
                .perform(post("/api/v1/countries")
                        .content(objectMapper.writeValueAsString(Country.builder()
                                .name("Armenia")
                                .build()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @SneakyThrows
    @WithMockUser(roles = {"ADMIN", "EDITOR"})
    void whenSaveAll_thenSuccess() {
        mockMvc
                .perform(post("/api/v1/countries/batch")
                        .content(objectMapper.writeValueAsString(
                                List.of(Country.builder()
                                        .name("Ukraine")
                                        .build())))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    @SneakyThrows
    @WithMockUser
    void whenSaveAll_thenForbidden() {
        mockMvc
                .perform(post("/api/v1/countries/batch")
                        .content(objectMapper.writeValueAsString(
                                List.of(Country.builder()
                                        .name("Ukraine")
                                        .build())))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @SneakyThrows
    @WithMockUser(roles = {"ADMIN", "EDITOR"})
    void whenEdit_thenSuccess() {
        mockMvc
                .perform(put("/api/v1/countries/{id}", 1)
                        .content(objectMapper.writeValueAsString(Country.builder()
                                .name("Czech")
                                .build()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @SneakyThrows
    @WithMockUser
    void whenEdit_thenForbidden() {
        mockMvc
                .perform(put("/api/v1/countries/{id}", 2)
                        .content(objectMapper.writeValueAsString(
                                Country.builder()
                                        .name("Czech")
                                        .build()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }
}