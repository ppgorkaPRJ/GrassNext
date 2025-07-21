package com.grassnext.grassnextserver.common.weatherstability;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The WeatherStability class represents a class of weather stability. It is a JPA entity used for persistence in the database.
 *
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WeatherStability {
    /**
     * The unique identifier for the WeatherStability entity.
     * This field is the primary key of the entity and is auto-generated
     * using the GenerationType.IDENTITY strategy.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * Name of weather stability class.
     *
     */
    @Column(nullable = false)
    private String weatherStability;
    /**
     * Additional textual description of weather stability.
     *
     */
    @Column
    private String description;

    /**
     * Constructs a new instance of the WeatherStability class.
     *
     * @param id             the unique identifier for the pollution type
     * @param weatherStability  the name of the weather stability class
     */
    public WeatherStability(Long id, String weatherStability) {
        this.id = id;
        this.weatherStability = weatherStability;
    }
}
