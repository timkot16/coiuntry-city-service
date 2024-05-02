package com.andersen.lab.controller;

import com.andersen.lab.model.City;
import com.andersen.lab.service.CityService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequestMapping("/api/v1/cities")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CityController {

    CityService cityService;

    @GetMapping
    public ResponseEntity<Page<City>> getAll(@RequestParam(defaultValue = "0") int page,
                                             @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<City> cities = cityService.getAll(pageable);
        return ResponseEntity.ok().body(cities);
    }

    @GetMapping("/unique")
    public ResponseEntity<List<String>> getAllUniqueNames() {
        List<String> uniqueCityNames = cityService.getAllUniqueNames();
        return ResponseEntity.ok().body(uniqueCityNames);
    }

    @GetMapping("/country/{countryName}")
    public ResponseEntity<List<City>> getAllByCountryName(@PathVariable String countryName) {
        List<City> cities = cityService.getAllByCountryName(countryName);
        return ResponseEntity.ok().body(cities);
    }

    @GetMapping("/{cityName}")
    public ResponseEntity<List<City>> getAllByName(@PathVariable String cityName) {
        List<City> cities = cityService.getAllByName(cityName);
        return ResponseEntity.ok().body(cities);
    }

    @SneakyThrows
    @PreAuthorize("hasAnyRole('ADMIN', 'EDITOR')")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<City> save(@RequestPart City city,
                                     @RequestPart(required = false) MultipartFile logo) {
        City savedCity = cityService.save(city, logo);
        return ResponseEntity.status(CREATED).body(savedCity);
    }

    @SneakyThrows
    @PreAuthorize("hasAnyRole('ADMIN', 'EDITOR')")
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> edit(@PathVariable Long id,
                                     @RequestPart City city,
                                     @RequestPart(required = false) MultipartFile logo) {
        cityService.edit(id, city, logo);
        return ResponseEntity.ok().build();
    }

    @SneakyThrows
    @GetMapping(value = "/{id}/logo", produces = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<byte[]> getLogo(@PathVariable Long id) {
        byte[] logo = cityService.getLogo(id);
        return ResponseEntity.ok().contentType(MediaType.MULTIPART_FORM_DATA).body(logo);
    }

}
