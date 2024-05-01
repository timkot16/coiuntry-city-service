package com.andersen.lab.model;

import lombok.Builder;

import java.io.Serializable;

@Builder
public record AuthenticationResponse(String token) implements Serializable {
}
