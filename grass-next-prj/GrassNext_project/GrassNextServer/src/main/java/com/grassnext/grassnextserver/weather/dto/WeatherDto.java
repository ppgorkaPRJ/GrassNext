package com.grassnext.grassnextserver.weather.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * WeatherDto is a Data Transfer Object (DTO) representing a pollution type.
 *
 */
@Data
@NoArgsConstructor
public class WeatherDto {
    /**
     * Wind speed in kilometers per hour.
     */
    private double windSpeed;
    /**
     * Wind direction in degrees.
     */
    private int windDirection;
}