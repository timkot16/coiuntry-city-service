package com.andersen.lab.service;

import com.andersen.lab.entity.CountryEntity;
import com.andersen.lab.mapper.CountryMapper;
import com.andersen.lab.model.Country;
import com.andersen.lab.repository.CountryRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CountryServiceTest {

    @InjectMocks
    CountryService countryService;

    @Mock
    CountryMapper countryMapper;
    @Mock
    CountryRepository countryRepository;

    private CountryEntity countryEntity;
    private Country country;
    private List<Country> countries = new ArrayList<>();
    private Pageable pageable;

    @BeforeEach
    void setUp() {
        pageable = PageRequest.of(0, 10);
        countryEntity = CountryEntity.builder().id(1L).name("Georgia").build();
        country = Country.builder().id(1L).name("Georgia").build();
        countries.add(country);
    }

    @Test
    void whenGetAll_thenSuccess_withEmptyCollection() {
        when(countryRepository.findAll(pageable)).thenReturn((Page.empty()));

        Page<Country> countries1 = countryService.getAll(pageable);

        assertEquals(1, countries1.getTotalPages());
        assertEquals(0, countries1.getTotalElements());
        assertEquals(Collections.emptyList(), countries1.getContent());
    }

    @Test
    void whenGetAll_thenSuccess_withNotEmptyCollection() {
        when(countryRepository.findAll(pageable)).thenReturn(new PageImpl<>(List.of(countryEntity)));
        when(countryMapper.toDto(any())).thenReturn(country);

        Page<Country> countries1 = countryService.getAll(pageable);

        assertEquals(1, countries1.getTotalPages());
        assertEquals(1, countries1.getTotalElements());
        assertEquals(countries, countries1.getContent());
    }

    @Test
    void whenGetByName_thenSuccess() {
        when(countryRepository.findByName(anyString())).thenReturn(Optional.of(countryEntity));

        CountryEntity georgia = countryService.getByName("Georgia");

        assertNotNull(georgia);
    }

    @Test
    void getByName_thenEntityNotFoundExceptionThrown() {
        when(countryRepository.findByName(anyString())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> countryService.getByName("Georgia"));
    }

    @Test
    void whenSave_thenSuccess() {
        Country countryToSave = Country.builder().name("Belarus").build();
        CountryEntity mappedCountryEntity = CountryEntity.builder().name("Belarus").build();
        CountryEntity savedCountryEntity = CountryEntity.builder().id(1L).name("Belarus").build();
        Country mappedCountry = Country.builder().id(1L).name("Belarus").build();
        when(countryMapper.toEntity(any())).thenReturn(mappedCountryEntity);
        when(countryRepository.save(any())).thenReturn(savedCountryEntity);
        when(countryMapper.toDto(any())).thenReturn(mappedCountry);

        Country country1 = countryService.save(countryToSave);

        assertNotNull(country1);
    }

    @Test
    void whenSave_thenDataIntegrityViolationExceptionThrown() {
        Country countryToSave = Country.builder().name("Belarus").build();
        CountryEntity mappedCountryEntity = CountryEntity.builder().name("Belarus").build();
        when(countryMapper.toEntity(any())).thenReturn(mappedCountryEntity);
        when(countryRepository.save(any())).thenThrow(DataIntegrityViolationException.class);

        assertThrows(DataIntegrityViolationException.class, () -> countryService.save(countryToSave));
    }

    @Test
    void whenSaveAll_thenSuccess() {
        List<Country> countriesToSave = List.of(
                Country.builder().name("France").build(),
                Country.builder().name("England").build()
        );
        List<CountryEntity> mappedCountryEntities = List.of(
                CountryEntity.builder().name("France").build(),
                CountryEntity.builder().name("England").build()
        );
        List<CountryEntity> savedCountryEntities = List.of(
                CountryEntity.builder().id(1L).name("France").build(),
                CountryEntity.builder().id(2L).name("England").build()
        );
        List<Country> mappedCountries = List.of(
                Country.builder().id(1L).name("France").build(),
                Country.builder().id(2L).name("England").build()
        );
        when(countryMapper.toEntityList(any())).thenReturn(mappedCountryEntities);
        when(countryRepository.saveAll(any())).thenReturn(savedCountryEntities);
        when(countryMapper.toDtoList(any())).thenReturn(mappedCountries);

        List<Country> countryList = countryService.saveAll(countriesToSave);

        assertEquals(2, countryList.size());
    }

    @Test
    void whenEdit_thenSuccess() {
        when(countryRepository.findById(anyLong())).thenReturn(Optional.ofNullable(countryEntity));
        when(countryRepository.save(any())).thenReturn(countryEntity);

        assertDoesNotThrow(() -> countryService.edit(1L, country));
    }

    @Test
    void whenEdit_thenEntityNotFoundExceptionThrown() {
        when(countryRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> countryService.edit(1L, country));

        verify(countryRepository, never()).save(any());
    }

}