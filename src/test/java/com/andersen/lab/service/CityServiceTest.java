package com.andersen.lab.service;

import com.andersen.lab.entity.CityEntity;
import com.andersen.lab.entity.CountryEntity;
import com.andersen.lab.mapper.CityMapper;
import com.andersen.lab.model.City;
import com.andersen.lab.model.Country;
import com.andersen.lab.repository.CityRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockMultipartFile;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CityServiceTest {

    private static final UUID LOGO_ID = UUID.randomUUID();

    @InjectMocks
    CityService cityService;
    @Mock
    CityMapper cityMapper;
    @Mock
    CityRepository cityRepository;
    @Mock
    CountryService countryService;
    @Mock
    S3Service s3Service;

    private CountryEntity countryEntity;
    private CityEntity cityEntity;
    private Country country;
    private City city;
    private Pageable pageable;

    @BeforeEach
    void setUp() {
        pageable = PageRequest.of(0, 10);
        countryEntity = CountryEntity.builder().id(1L).name("Georgia").build();
        cityEntity = CityEntity.builder().id(1L).name("Batumi").countryEntity(countryEntity).logoId(LOGO_ID).build();
        country = Country.builder().id(1L).name("Georgia").build();
        city = City.builder().id(1L).cityName("Batumi").country(country).build();
    }

    @Test
    void whenGetAll_thenSuccess_withEmptyCollection() {
        when(cityRepository.findAll(pageable)).thenReturn((Page.empty()));

        Page<City> cities = cityService.getAll(pageable);

        assertEquals(1, cities.getTotalPages());
        assertEquals(0, cities.getTotalElements());
        assertEquals(Collections.emptyList(), cities.getContent());
    }

    @Test
    void whenGetAll_thenSuccess_withNotEmptyCollection() {
        when(cityRepository.findAll(pageable)).thenReturn(new PageImpl<>(List.of(cityEntity)));
        when(cityMapper.toDto(any())).thenReturn(city);

        Page<City> cities = cityService.getAll(pageable);

        assertEquals(1, cities.getTotalPages());
        assertEquals(1, cities.getTotalElements());
    }

    @Test
    void whenGetAllUniqueNames_thenSuccess_withEmptyCollection() {
        when(cityRepository.findAllUniqueCityNames()).thenReturn(Collections.emptyList());

        List<String> allUniqueNames = cityService.getAllUniqueNames();

        assertTrue(allUniqueNames.isEmpty());
    }

    @Test
    void whenGetAllUniqueNames_thenSuccess_withNotEmptyCollection() {
        when(cityRepository.findAllUniqueCityNames()).thenReturn(Collections.singletonList("Brest"));

        List<String> allUniqueNames = cityService.getAllUniqueNames();

        assertEquals(1, allUniqueNames.size());
    }

    @Test
    void whenGetAllByCountryName_andCountryExist_thenSuccess() {
        when(countryService.getByName(anyString())).thenReturn(countryEntity);
        when(cityRepository.findAllByCountryEntityId(anyLong())).thenReturn(List.of(cityEntity));
        when(cityMapper.toDto(any())).thenReturn(city);

        List<City> cities = cityService.getAllByCountryName("Georgia");

        assertEquals(1, cities.size());
    }

    @Test
    void whenGetAllByName_thenSuccess() {
        when(cityRepository.findAllByName(anyString())).thenReturn(List.of(cityEntity));

        List<City> cities = cityService.getAllByName("Batumi");

        assertEquals(1, cities.size());
    }

    @Test
    void whenGetAllByName_thenSuccess_withEmptyCollection() {
        when(cityRepository.findAllByName(anyString())).thenReturn(Collections.emptyList());

        List<City> cities = cityService.getAllByName("Brest");

        assertEquals(0, cities.size());
    }

    @Test
    void whenEdit_withoutLogo_thenSuccess() {
        City city = City.builder().cityName("Tbilisi").build();

        when(cityRepository.findById(1L)).thenReturn(Optional.of(cityEntity));

        cityService.edit(1L, city, null);

        verify(s3Service, never()).putContent(anyString(), any());
    }

    @Test
    void whenEdit_withLogo_thenSuccess() {
        City city = City.builder().cityName("Tbilisi").build();
        MockMultipartFile multipartFile = new MockMultipartFile("test.png", "test".getBytes());

        when(cityRepository.findById(1L)).thenReturn(Optional.of(cityEntity));

        cityService.edit(1L, city, multipartFile);

        verify(s3Service, times(1)).putContent(LOGO_ID.toString(), multipartFile);
    }

    @Test
    void whenEdit_andEntityNotPresent_thenDoNothing() {
        City city = City.builder().cityName("Kalahuri").build();
        MockMultipartFile multipartFile = new MockMultipartFile("test.png", "test".getBytes());
        when(cityRepository.findById(1L)).thenReturn(Optional.empty());

        cityService.edit(1L, city, multipartFile);

        verify(cityRepository, never()).save(any());
    }

    @Test
    void whenSave_thenSuccess() {
        MockMultipartFile multipartFile = new MockMultipartFile("test.png", "test".getBytes());
        City cityToSave = City.builder()
                .cityName("Tbilisi")
                .country(Country.builder().name("Georgia").build())
                .build();
        CityEntity mappedCityEntity = CityEntity.builder()
                .name("Tbilisi")
                .logoId(LOGO_ID)
                .countryEntity(countryEntity)
                .build();
        CityEntity savedCityEntity = CityEntity.builder()
                .id(1L)
                .name("Tbilisi")
                .logoId(LOGO_ID)
                .countryEntity(countryEntity)
                .build();
        City mappedCity = City.builder()
                .id(1L)
                .cityName("Tbilisi")
                .country(country)
                .build();

        when(countryService.getByName(anyString())).thenReturn(countryEntity);
        when(cityMapper.toEntity(any(), any())).thenReturn(mappedCityEntity);
        when(cityRepository.save(any())).thenReturn(savedCityEntity);
        when(cityMapper.toDto(any())).thenReturn(mappedCity);

        City newCity = cityService.save(cityToSave, multipartFile);

        assertEquals("Tbilisi", newCity.cityName());
        assertEquals("Georgia", newCity.country().name());

        verify(s3Service, times(1)).putContent(LOGO_ID.toString(), multipartFile);
    }

    @Test
    void whenGetLogo_thenSuccess() {
        byte[] expectedLogo = new byte[]{1, 2, 3};
        when(cityRepository.findById(1L)).thenReturn(Optional.of(cityEntity));
        when(s3Service.getContent(anyString())).thenReturn(expectedLogo);

        byte[] logo = cityService.getLogo(1L);

        assertNotNull(logo);
        assertArrayEquals(expectedLogo, logo);
    }

    @Test
    void whenGetLogo_thenEntityNotFoundExceptionThrown() {
        when(cityRepository.findById(1L)).thenThrow(EntityNotFoundException.class);

        assertThrows(EntityNotFoundException.class, () -> cityService.getLogo(1L));
    }
}