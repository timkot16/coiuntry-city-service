package com.andersen.lab.model;

import com.andersen.lab.entity.CountryEntity;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.io.Serializable;

/**
 * DTO for {@link CountryEntity}
 */
@Builder
public record Country(@JsonProperty(access = JsonProperty.Access.READ_ONLY) Long id,
                      String name) implements Serializable {
}