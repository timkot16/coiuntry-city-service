package com.andersen.lab.mapper;

import com.andersen.lab.entity.CityEntity;
import com.andersen.lab.entity.CountryEntity;
import com.andersen.lab.model.City;
import com.andersen.lab.model.Country;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CityMapperTest {
    private final CountryMapper countryMapper = Mappers.getMapper(CountryMapper.class);

    private final CityMapper cityMapper = Mappers.getMapper(CityMapper.class);

    private final Country country = Country.builder()
            .id(1L)
            .name("France")
            .build();
    private final City city = City.builder()
            .id(1L)
            .cityName("Brest")
            .country(country)
            .build();

    private final CountryEntity countryEntity = CountryEntity.builder()
            .id(1L)
            .name("France")
            .build();
    private final CityEntity cityEntity = CityEntity.builder()
            .id(1L)
            .logoId(UUID.randomUUID())
            .name("Brest")
            .countryEntity(countryEntity)
            .build();

    @Test
    void whenToEntity_thenSuccess() {
        CityEntity entity = cityMapper.toEntity(city, countryEntity);

        assertEquals(city.cityName(), cityEntity.getName());
        assertNull(entity.getId());
        assertNotNull(entity.getLogoId());
        assertNotNull(entity.getCountryEntity());

    }

    @Test
    void whenToEntity_thenNullReturned() {
        assertNull(cityMapper.toEntity(null, null));
    }

    @Test
    void whenToDto_thenSuccess() {
        ReflectionTestUtils.setField(cityMapper, "countryMapper", countryMapper);
        City cityDto = cityMapper.toDto(cityEntity);

        assertNotNull(cityDto);
        assertEquals(cityEntity.getId(), cityDto.id());
        assertEquals(cityEntity.getName(), cityDto.cityName());
        assertNotNull(cityDto.country());
        assertEquals(cityEntity.getCountryEntity().getId(), cityDto.country().id());
        assertEquals(cityEntity.getCountryEntity().getName(), cityDto.country().name());

    }

    @Test
    void whenToDto_thenNullReturned() {
        assertNull(cityMapper.toDto(null));
    }

}