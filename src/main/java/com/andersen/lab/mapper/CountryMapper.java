package com.andersen.lab.mapper;

import com.andersen.lab.entity.CityEntity;
import com.andersen.lab.entity.CountryEntity;
import com.andersen.lab.model.City;
import com.andersen.lab.model.Country;
import org.mapstruct.*;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING)
public interface CountryMapper {

    @Mapping(target = "id", ignore = true)
    CountryEntity toEntity(Country country);

    Country toDto(CountryEntity countryEntity);

    List<CountryEntity> toEntityList(List<Country> countries);

    List<Country> toDtoList(List<CountryEntity> countryEntities);

}