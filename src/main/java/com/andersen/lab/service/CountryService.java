package com.andersen.lab.service;

import com.andersen.lab.entity.CountryEntity;
import com.andersen.lab.mapper.CountryMapper;
import com.andersen.lab.model.Country;
import com.andersen.lab.repository.CountryRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CountryService {

    CountryMapper countryMapper;
    CountryRepository countryRepository;

    public Page<Country> getAll(Pageable pageable) {
        return countryRepository.findAll(pageable)
                .map(countryMapper::toDto);
    }

    public CountryEntity getByName(String name) {
        return countryRepository.findByName(name)
                .orElseThrow(() -> new EntityNotFoundException("Country with name [" + name + "] doesn't exist in DB"));
    }

    @Transactional
    public Country save(Country country) {
        log.info("Try to save new county with name [{}]", country.name());
        CountryEntity newCountryEntity = countryMapper.toEntity(country);
        CountryEntity countryEntity = countryRepository.save(newCountryEntity);
        return countryMapper.toDto(countryEntity);
    }

    @Transactional
    public List<Country> saveAll(List<Country> countries) {
        log.info("Try to save batch of counties [{}]", countries.toString());
        List<CountryEntity> newCountryEntities = countryMapper.toEntityList(countries);
        List<CountryEntity> countryEntities = countryRepository.saveAll(newCountryEntities);
        return countryMapper.toDtoList(countryEntities);
    }

    @Transactional
    public void edit(Long id, Country country) {
        log.info("Try to edit country [{}]", country.name());
        Optional<CountryEntity> optionalCountryEntity = countryRepository.findById(id);
        if (optionalCountryEntity.isPresent()) {
            CountryEntity countryEntity = optionalCountryEntity.get();
            countryEntity.setName(country.name());
            countryRepository.save(countryEntity);
            return;
        }
        throw new EntityNotFoundException("Country with id [" + id + "] doesn't exist in DB");
    }

}
