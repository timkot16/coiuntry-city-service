package com.andersen.lab.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

import java.io.Serializable;

@Builder
public record RegisterRequest(String firstName,
                              String lastName,
                              @Email @NotBlank String email,
                              @NotBlank String password) implements Serializable {
}
