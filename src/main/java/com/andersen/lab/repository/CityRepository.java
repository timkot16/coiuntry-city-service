package com.andersen.lab.repository;

import com.andersen.lab.entity.CityEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CityRepository extends JpaRepository<CityEntity, Long> {
    List<CityEntity> findAllByCountryEntityId(Long countryEntityId);

    List<CityEntity> findAllByName(String cityName);

    @Query("SELECT DISTINCT c.name FROM CityEntity c")
    List<String> findAllUniqueCityNames();

}