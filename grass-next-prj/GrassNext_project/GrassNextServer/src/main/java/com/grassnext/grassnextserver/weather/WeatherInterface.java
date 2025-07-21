package com.grassnext.grassnextserver.weather;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.time.LocalDateTime;
import java.util.List;

/**
 * The WeatherInterface specifies a method to interact with weather data, providing functionality
 * for retrieving weather-related information such as wind speed and direction at a specific
 * geographical location and time range.
 */
public interface WeatherInterface {
    /**
     * Retrieves a list of weather data, including wind speed and direction, for a given geographical location
     * and specified time range.
     *
     * @param longitude the geographical longitude of the desired location
     * @param latitude the geographical latitude of the desired location
     * @param startDate the start of the time range for retrieving weather data
     * @param endDate the end of the time range for retrieving weather data
     * @return a list of Weather objects containing wind speed and direction data for the specified location and time range
     * @throws JsonProcessingException if an error occurs while processing JSON data
     */
    List<Weather> getWindSpeedAndDirection(double longitude, double latitude, LocalDateTime startDate, LocalDateTime endDate) throws JsonProcessingException;
}