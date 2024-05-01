package com.andersen.lab.integration.repository;

import com.andersen.lab.entity.CityEntity;
import com.andersen.lab.entity.CountryEntity;
import com.andersen.lab.integration.IntegrationTestBase;
import com.andersen.lab.repository.CityRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@RequiredArgsConstructor
class CityRepositoryTest extends IntegrationTestBase {

    private final CityRepository cityRepository;

    @Test
    void whenFindAllByCountryEntityId_thenSuccess() {
        List<CityEntity> allByCountryEntityId = cityRepository.findAllByCountryEntityId(2L);

        assertEquals(2, allByCountryEntityId.size());
        allByCountryEntityId.forEach(cityEntity -> {
            assertNotNull(cityEntity.getId());
            assertNotNull(cityEntity.getName());
            assertNotNull(cityEntity.getLogoId());
            assertNotNull(cityEntity.getCountryEntity());
        });
    }

    @Test
    void whenFindAllByName_thenSuccess() {
        List<CityEntity> cities = cityRepository.findAllByName("Brest");

        assertNotNull(cities);
        assertEquals(2, cities.size());
    }

    @Test
    void whenFindAllUniqueCityNames_thenSuccess() {
        List<String> allUniqueCityNames = cityRepository.findAllUniqueCityNames();

        assertNotNull(allUniqueCityNames);
        assertEquals(4, allUniqueCityNames.size());
    }

    @Test
    void whenSave_thenSuccess() {
        CountryEntity countryEntity = CountryEntity.builder().id(2L).name("Belarus").build();
        CityEntity cityEntity = CityEntity.builder()
                .name("Madrid")
                .logoId(UUID.randomUUID())
                .countryEntity(countryEntity)
                .build();

        CityEntity save = cityRepository.save(cityEntity);

        assertEquals(7L, save.getId());
    }

    @Test
    void whenSave_thenDataIntegrityViolationExceptionThrown() {
        CountryEntity countryEntity = CountryEntity.builder().id(2L).name("Belarus").build();
        CityEntity cityEntity = CityEntity.builder()
                .name("Brest")
                .logoId(UUID.randomUUID())
                .countryEntity(countryEntity)
                .build();

        assertThrows(DataIntegrityViolationException.class, () -> cityRepository.save(cityEntity));
    }

}