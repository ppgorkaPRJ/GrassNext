package com.grassnext.grassnextserver.common.weatherstability.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * WeatherStabilityDto is a Data Transfer Object (DTO) representing a weather stability type.
 *
 */
@Data
@NoArgsConstructor
public class WeatherStabilityDto {
    /**
     * The unique identifier for the WeatherStabilityDto.
     *
     */
    private Long id;
    /**
     * Name of weather stability class.
     *
     */
    private String weatherStability;
    /**
     * Additional textual description of weather stability.
     *
     */
    private String description;
}
