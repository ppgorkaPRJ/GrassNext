package com.grassnext.grassnextserver.weather;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * The Weather class represents weather data. It is a JPA entity used for persistence in the database.
 *
 */
@Entity
@IdClass(WeatherId.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Weather {
    /**
     * Represents the date on which the weather measurement was taken.
     *
     */
    @Id
    @Column(nullable = false)
    private LocalDate measurementDate;
    /**
     * Represents the specific hour of the day for which the weather measurement was taken.
     *
     */
    @Id
    @Column(nullable = false)
    private int measurementHour;
    /**
     * Represents the geographical longitude of a location for which the weather measurement was taken.
     *
     */
    @Id
    @Column(nullable = false)
    private double longitude;
    /**
     * Represents the geographical latitude of a location for which the weather measurement was taken.
     *
     */
    @Id
    @Column(nullable = false)
    private double latitude;
    /**
     * Represents the wind speed for a specific weather measurement entry.
     *
     */
    @Column(nullable = false)
    private double windSpeed;
    /**
     * Represents the direction of the wind, measured in degrees.
     *
     */
    @Column(nullable = false)
    private int windDirection;
}