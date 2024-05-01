package com.andersen.lab.mapper;

import com.andersen.lab.entity.CountryEntity;
import com.andersen.lab.model.Country;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CountryMapperTest {

    private final CountryMapper countryMapper = Mappers.getMapper(CountryMapper.class);

    private final Country country = Country.builder()
            .name("France")
            .build();

    private final CountryEntity countryEntity = CountryEntity.builder()
            .id(1L)
            .name("France")
            .build();

    @Test
    void whenToEntity_thenSuccess() {
        CountryEntity entity = countryMapper.toEntity(country);

        assertNotNull(entity);
        assertEquals(country.name(), entity.getName());
        assertNull(entity.getId());
    }

    @Test
    void whenToEntity_thenNullReturned() {
        assertNull(countryMapper.toEntity(null));
    }


    @Test
    void whenToDto_thenSuccess() {
        Country dto = countryMapper.toDto(countryEntity);

        assertNotNull(dto);
        assertEquals(countryEntity.getId(), dto.id());
        assertEquals(countryEntity.getName(), dto.name());
    }

    @Test
    void whenToDto_thenNullReturned() {
        assertNull(countryMapper.toDto(null));
    }

}