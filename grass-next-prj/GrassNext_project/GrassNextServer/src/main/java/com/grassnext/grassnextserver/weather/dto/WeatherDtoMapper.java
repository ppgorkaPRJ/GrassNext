package com.grassnext.grassnextserver.weather.dto;

import com.grassnext.grassnextserver.weather.Weather;
import org.springframework.stereotype.Service;

/**
 * A service responsible for mapping {@link Weather} entities to {@link WeatherDto} objects.
 *
 */
@Service
public class WeatherDtoMapper {
    /**
     * Maps a {@link Weather} entity to a {@link WeatherDto} Data Transfer Object.
     *
     * @param weather the {@link Weather} entity to be mapped
     * @return a {@link WeatherDto} containing the mapped data from the {@link Weather} entity
     */
    public WeatherDto map(Weather weather) {
        WeatherDto dto = new WeatherDto();
        dto.setWindSpeed(weather.getWindSpeed());
        dto.setWindDirection(weather.getWindDirection());

        return dto;
    }
}
