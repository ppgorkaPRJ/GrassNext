package com.grassnext.grassnextserver.common.weatherstability.dto;

import com.grassnext.grassnextserver.common.weatherstability.WeatherStability;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * A service responsible for mapping {@link WeatherStability} entities to {@link WeatherStabilityDto} objects.
 *
 */
@Service
public class WeatherStabilityDtoMapper {
    /**
     * Maps a {@link WeatherStability} entity to a {@link WeatherStabilityDto} Data Transfer Object.
     *
     * @param weatherStability the {@link WeatherStability} entity to be mapped
     * @return a {@link WeatherStabilityDto} containing the mapped data from the {@link WeatherStability} entity
     */
    public WeatherStabilityDto map(WeatherStability weatherStability) {
        WeatherStabilityDto dto = new WeatherStabilityDto();
        dto.setId(weatherStability.getId());
        dto.setWeatherStability(weatherStability.getWeatherStability());
        dto.setDescription(weatherStability.getDescription());

        return dto;
    }

    /**
     * Maps a list of {@link WeatherStability} entities to a list of {@link WeatherStabilityDto} objects.
     *
     * @param weatherStabilityList the list of {@link WeatherStability} entities to be mapped; must not be null
     * @return a list of {@link WeatherStabilityDto} objects representing the mapped entities;
     *         an empty list is returned if the input list is empty or contains only null elements
     */
    public List<WeatherStabilityDto> mapAll(List<WeatherStability> weatherStabilityList) {
        List<WeatherStabilityDto> weatherStabilityDtoList = new ArrayList<>();
        if (!weatherStabilityList.isEmpty()) {
            for (WeatherStability weatherStability : weatherStabilityList) {
                if (weatherStability != null) {
                    weatherStabilityDtoList.add( map(weatherStability) );
                }
            }
        }

        return weatherStabilityDtoList;
    }
}
