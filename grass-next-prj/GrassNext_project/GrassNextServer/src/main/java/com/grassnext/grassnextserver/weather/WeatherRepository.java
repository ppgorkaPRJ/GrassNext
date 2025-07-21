package com.grassnext.grassnextserver.weather;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

/**
 * Repository interface for Weather entities.
 *
 */
public interface WeatherRepository extends JpaRepository<Weather, Long>  {
    Weather findWeatherByMeasurementDateAndMeasurementHourAndLongitudeAndLatitude(LocalDate measurementDate, int measurementHour, double longitude, double latitude);
}
