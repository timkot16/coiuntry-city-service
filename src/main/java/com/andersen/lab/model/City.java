package com.andersen.lab.model;

import com.andersen.lab.entity.CityEntity;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.io.Serializable;

/**
 * DTO for {@link CityEntity}
 */
@Builder
public record City(
        @JsonProperty(access = JsonProperty.Access.READ_ONLY)
        Long id,
        String cityName,
        Country country) implements Serializable {
}