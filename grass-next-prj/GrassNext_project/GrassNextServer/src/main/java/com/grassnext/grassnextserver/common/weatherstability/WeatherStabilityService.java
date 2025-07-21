package com.grassnext.grassnextserver.common.weatherstability;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class responsible for managing REST operations related to WeatherStability entities.
 *
 */
@Service
@Data
@NoArgsConstructor
public class WeatherStabilityService {
    /**
     * Dependency-injected repository used to perform CRUD operations on WeatherStability entities.
     *
     */
    WeatherStabilityRepository weatherStabilityRepository;

    /**
     * Constructs a new instance of WeatherStabilityService.
     *
     * @param weatherStabilityRepository the repository used for CRUD operations on {@link WeatherStability} entities
     *
     */
    @Autowired
    WeatherStabilityService(WeatherStabilityRepository weatherStabilityRepository) {
        this.weatherStabilityRepository = weatherStabilityRepository;
    }

    /**
     * Retrieves all weather stability classes from the data repository.
     *
     * @return a list of WeatherStability objects representing all the weather stability classes in the database
     */
    public List<WeatherStability> getAllWeatherStabilities() {
        return weatherStabilityRepository.findAll();
    }
}
