package com.andersen.lab.integration.repository;

import com.andersen.lab.entity.CountryEntity;
import com.andersen.lab.integration.IntegrationTestBase;
import com.andersen.lab.repository.CountryRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@RequiredArgsConstructor
class CountryRepositoryTest extends IntegrationTestBase {

    private final CountryRepository countryRepository;

    @Test
    void whenFindByName_thenSuccess() {
        Optional<CountryEntity> belarus = countryRepository.findByName("Belarus");

        assertFalse(belarus.isEmpty());
    }

    @Test
    void whenFindByName_thenOptionalEmpty() {
        Optional<CountryEntity> england = countryRepository.findByName("England");

        assertTrue(england.isEmpty());
    }

    @Test
    void whenSave_thenSuccess() {
        CountryEntity countryEntity = CountryEntity.builder().name("England").build();
        CountryEntity save = countryRepository.save(countryEntity);

        assertNotNull(save);
        assertEquals(5L, save.getId());
    }

    @Test
    void whenSave_thenDataIntegrityViolationExceptionThrown() {
        CountryEntity countryEntity = CountryEntity.builder().name("USA").build();

        assertThrows(DataIntegrityViolationException.class, () -> countryRepository.save(countryEntity));
    }

}