package com.andersen.lab.model;

import lombok.Builder;

import java.io.Serializable;

@Builder
public record ErrorResponse(String message, String details) implements Serializable {
}
