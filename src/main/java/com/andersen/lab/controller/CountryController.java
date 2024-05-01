package com.andersen.lab.controller;

import com.andersen.lab.model.Country;
import com.andersen.lab.service.CountryService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequestMapping("/api/v1/countries")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CountryController {

    CountryService countryService;

    @GetMapping
    public ResponseEntity<List<Country>> getAll(@RequestParam(defaultValue = "0") int page,
                                                @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Country> cities = countryService.getAll(pageable);
        return ResponseEntity.ok().body(cities.getContent());
    }

    @SneakyThrows
    @PreAuthorize("hasAnyRole('ADMIN', 'EDITOR')")
    @PostMapping
    public ResponseEntity<Country> save(@RequestBody Country country) {
        Country savedCountry = countryService.save(country);
        return ResponseEntity.status(CREATED).body(savedCountry);
    }

    @SneakyThrows
    @PreAuthorize("hasAnyRole('ADMIN', 'EDITOR')")
    @PostMapping("/batch")
    public ResponseEntity<List<Country>> saveAll(@RequestBody List<Country> countries) {
        List<Country> countryList = countryService.saveAll(countries);
        return ResponseEntity.status(CREATED).body(countryList);
    }

    @SneakyThrows
    @PreAuthorize("hasAnyRole('ADMIN', 'EDITOR')")
    @PutMapping(value = "/{id}")
    public ResponseEntity<Void> edit(@PathVariable Long id,
                                     @RequestBody Country country) {
        countryService.edit(id, country);
        return ResponseEntity.ok().build();
    }

}
