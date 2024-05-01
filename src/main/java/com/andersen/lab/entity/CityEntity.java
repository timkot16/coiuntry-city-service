package com.andersen.lab.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "cities", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"name", "country_entity_id"}, name = "unique_city_name_within_country")
})
public class CityEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "logo_id", unique = true)
    private UUID logoId;

    @ManyToOne
    @JoinColumn(name = "country_entity_id")
    private CountryEntity countryEntity;

}