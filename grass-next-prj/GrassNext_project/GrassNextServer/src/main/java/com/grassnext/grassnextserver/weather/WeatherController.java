package com.grassnext.grassnextserver.weather;

import com.grassnext.grassnextserver.weather.dto.WeatherDtoMapper;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The WeatherController serves as a REST controller for handling API requests
 * related to weather data.
 *
 */
@RestController
@RequestMapping("/api/weather/")
@NoArgsConstructor
class WeatherController {
    /**
     * Service instance for managing weather data.
     *
     */
    OpenMeteoService openMeteoService;
    /**
     * Maps weather data entities to Data Transfer Objects (DTOs).
     *
     */
    WeatherDtoMapper weatherDtoMapper;

    /**
     * Constructs a new instance of the WeatherController.
     *
     * @param openMeteoService the service for managing weather data
     * @param weatherDtoMapper the mapper used to transform weather data entities to DTOs
     */
    @Autowired
    public WeatherController(
            OpenMeteoService openMeteoService,
            WeatherDtoMapper weatherDtoMapper
    ) {
        this.openMeteoService = openMeteoService;
        this.weatherDtoMapper = weatherDtoMapper;
    }
}
