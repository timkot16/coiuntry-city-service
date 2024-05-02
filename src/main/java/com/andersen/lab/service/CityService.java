package com.andersen.lab.service;

import com.andersen.lab.entity.CityEntity;
import com.andersen.lab.entity.CountryEntity;
import com.andersen.lab.mapper.CityMapper;
import com.andersen.lab.model.City;
import com.andersen.lab.repository.CityRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CityService {

    CityMapper cityMapper;
    CityRepository cityRepository;
    CountryService countryService;
    S3Service s3Service;

    public Page<City> getAll(Pageable pageable) {
        log.info("Try to get all cities with pagination");
        return cityRepository.findAll(pageable)
                .map(cityMapper::toDto);
    }

    public List<String> getAllUniqueNames() {
        log.info("Try to get all only unique cities names");
        return cityRepository.findAllUniqueCityNames();
    }

    public List<City> getAllByCountryName(String countryName) {
        log.info("Try to get all cities by country name");
        CountryEntity countryEntity = countryService.getByName(countryName);
        return cityRepository.findAllByCountryEntityId(countryEntity.getId()).stream()
                .map(cityMapper::toDto)
                .toList();
    }

    public List<City> getAllByName(String cityName) {
        log.info("Try to get all cities by city name");
        return cityRepository.findAllByName(cityName).stream()
                .map(cityMapper::toDto)
                .toList();
    }

    @SneakyThrows
    @Transactional
    public void edit(Long cityId, City city, MultipartFile logo) {
        log.info("Try to edit city with id [{}] and name [{}]", cityId, city.cityName());
        cityRepository.findById(cityId)
                .ifPresent(cityEntity -> {
                    cityEntity.setName(city.cityName());
                    if (logo != null) {
                        UUID logoId = cityEntity.getLogoId() != null ?
                                cityEntity.getLogoId() :
                                UUID.randomUUID();
                        cityEntity.setLogoId(logoId);
                        s3Service.putContent(logoId.toString(), logo);
                    }
                    cityRepository.save(cityEntity);
                });
    }

    @Transactional
    public City save(City city, MultipartFile logo) {
        log.info("Try to save city with name [{}]", city.cityName());
        CountryEntity countryEntity = countryService.getByName(city.country().name());
        CityEntity cityEntity = cityMapper.toEntity(city, countryEntity);
        saveLogoIfExist(logo, cityEntity);
        CityEntity newCity = cityRepository.save(cityEntity);
        return cityMapper.toDto(newCity);
    }

    @SneakyThrows
    public byte[] getLogo(Long cityId) {
        log.info("Try to get logo by city id [{}]", cityId);
        return cityRepository.findById(cityId)
                .map(CityEntity::getLogoId)
                .map(uuid -> s3Service.getContent(uuid.toString()))
                .orElseThrow(EntityNotFoundException::new);
    }

    private void saveLogoIfExist(MultipartFile logo, CityEntity cityEntity) {
        if (logo != null) {
            s3Service.putContent(cityEntity.getLogoId().toString(), logo);
            return;
        }
        cityEntity.setId(null);
    }

}
