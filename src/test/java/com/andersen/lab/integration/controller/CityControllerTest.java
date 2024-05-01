package com.andersen.lab.integration.controller;

import com.andersen.lab.integration.IntegrationTestBase;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RequiredArgsConstructor
class CityControllerTest extends IntegrationTestBase {

    private static final MockMultipartFile LOGO = new MockMultipartFile(
            "logo",
            null,
            "application/octet-stream",
            "Logo".getBytes());

    private final MockMvc mockMvc;

    @Test
    @SneakyThrows
    @WithMockUser
    void whenGetAll_thenSuccess() {
        mockMvc
                .perform(get("/api/v1/cities")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk());
    }

    @Test
    @SneakyThrows
    @WithAnonymousUser
    void whenGetAll_thenForbidden() {
        mockMvc
                .perform(get("/api/v1/cities")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isForbidden());
    }

    @Test
    @SneakyThrows
    @WithMockUser
    void whenGetAllUniqueNames_thenSuccess() {
        mockMvc
                .perform(get("/api/v1/cities/unique"))
                .andExpect(status().isOk());
    }

    @Test
    @SneakyThrows
    @WithAnonymousUser
    void whenGetAllUniqueNames_thenForbidden() {
        mockMvc
                .perform(get("/api/v1/cities/unique"))
                .andExpect(status().isForbidden());
    }

    @Test
    @SneakyThrows
    @WithMockUser
    void whenGetAllByCountryName_thenSuccess() {
        mockMvc
                .perform(get("/api/v1/cities/{countryName}", "Belarus"))
                .andExpect(status().isOk());
    }

    @Test
    @SneakyThrows
    @WithAnonymousUser
    void whenGetAllByCountryName_thenForbidden() {
        mockMvc
                .perform(get("/api/v1/cities/{countryName}", "Belarus"))
                .andExpect(status().isForbidden());
    }

    @Test
    @SneakyThrows
    @WithMockUser
    void whenGetAllByName_thenSuccess() {
        mockMvc
                .perform(get("/api/v1/cities/{countryName}", "Belarus"))
                .andExpect(status().isOk());
    }

    @Test
    @SneakyThrows
    @WithAnonymousUser
    void whenGetAllByName_thenForbidden() {
        mockMvc
                .perform(get("/api/v1/cities/{countryName}", "Belarus"))
                .andExpect(status().isForbidden());
    }

    @Test
    @SneakyThrows
    @WithMockUser(roles = {"ADMIN", "EDITOR"})
    void whenSave_thenSuccess() {
        MockMultipartFile city = new MockMultipartFile(
                "city",
                null,
                "application/json",
                "{\"cityName\": \"Washington\", \"country\": {\"name\": \"USA\"}}".getBytes());

        mockMvc.perform(multipart("/api/v1/cities")
                        .file(city)
                        .file(LOGO))
                .andExpect(status().isCreated());
    }

    @Test
    @SneakyThrows
    @WithMockUser
    void whenSave_thenForbidden() {
        MockMultipartFile city = new MockMultipartFile(
                "city",
                null,
                "application/json",
                "{\"cityName\": \"Washington\", \"country\": {\"name\": \"Portugal\"}}".getBytes());

        mockMvc.perform(multipart("/api/v1/cities")
                        .file(city)
                        .file(LOGO))
                .andExpect(status().isForbidden());
    }

    @Test
    @SneakyThrows
    @WithMockUser(roles = {"ADMIN", "EDITOR"})
    void whenEdit_thenSuccess() {
        MockMultipartFile city = new MockMultipartFile(
                "city",
                null,
                "application/json",
                "{\"cityName\": \"Washington\", \"country\": {\"name\": \"USA\"}}".getBytes());

        mockMvc.perform(multipart("/api/v1/cities/{id}", 1)
                        .file(city)
                        .with(request -> {
                            request.setMethod(HttpMethod.PUT.name());
                            return request;
                        }))
                .andExpect(status().isOk());
    }

    @Test
    @SneakyThrows
    @WithMockUser
    void whenGetLogo_thenSuccess() {
        mockMvc.perform(get("/api/v1/cities/{id}/logo", 1))
                .andExpect(status().isBadRequest());
    }

}