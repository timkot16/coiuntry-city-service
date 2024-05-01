package com.andersen.lab.mapper;

import com.andersen.lab.entity.CityEntity;
import com.andersen.lab.entity.CountryEntity;
import com.andersen.lab.model.City;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        uses = {CountryMapper.class})
public interface CityMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "name", expression = "java(city.cityName())")
    @Mapping(target = "logoId", expression = "java(java.util.UUID.randomUUID())")
    @Mapping(target = "countryEntity.id", expression = "java(countryEntity.getId())")
    CityEntity toEntity(City city, CountryEntity countryEntity);

    @Mapping(target = "country", source = "countryEntity")
    @Mapping(target = "cityName", source = "name")
    City toDto(CityEntity cityEntity);

}