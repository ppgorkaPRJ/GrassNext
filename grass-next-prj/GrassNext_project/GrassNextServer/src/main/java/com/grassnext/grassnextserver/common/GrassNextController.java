package com.grassnext.grassnextserver.common;

import com.grassnext.grassnextserver.common.pollutiontype.PollutionTypeService;
import com.grassnext.grassnextserver.common.pollutiontype.dto.*;
import com.grassnext.grassnextserver.common.vehiclegroup.VehicleGroup;
import com.grassnext.grassnextserver.common.vehiclegroup.VehicleGroupService;
import com.grassnext.grassnextserver.common.vehiclegroup.dto.*;
import com.grassnext.grassnextserver.common.weatherstability.WeatherStabilityService;
import com.grassnext.grassnextserver.common.weatherstability.dto.*;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

/**
 * This controller handles various endpoints for the GrassNext application,
 * providing API functionality related to pollution types, vehicle groups,
 * and weather stability data.
 *
 * It defines endpoints under the base path "/api/" for retrieving server status
 * and fetching data relating to pollution types, vehicle groups, and weather stabilities.
 */
@RestController
@RequestMapping("/api/")
@NoArgsConstructor
public class GrassNextController {
    /**
     * Service instance for managing pollution type-related operations.
     *
     */
    PollutionTypeService pollutionTypeService;
    /**
     * Service layer responsible for managing and processing vehicle group data in the system.
     *
     */
    VehicleGroupService vehicleGroupService;
    /**
     * Service responsible for managing and retrieving weather stability data.
     *
     */
    WeatherStabilityService weatherStabilityService;

    /**
     * Maps pollution type entities to Data Transfer Objects (DTOs).
     *
     */
    PollutionTypeDtoMapper pollutionTypeDtoMapper;
    /**
     * Maps vehicle group entities to Data Transfer Objects (DTOs).
     *
     */
    VehicleGroupDtoMapper vehicleGroupDtoMapper;
    /**
     * Maps weather stability class entities to Data Transfer Objects (DTOs).
     *
     */
    WeatherStabilityDtoMapper weatherStabilityDtoMapper;

    /**
     * Constructs a new instance of the GrassNextController with the required dependencies for
     * handling pollution types, vehicle groups, and weather stability data.
     *
     * @param pollutionTypeService the service for managing and processing pollution type data
     * @param vehicleGroupService the service for managing and processing vehicle group data
     * @param weatherStabilityService the service for handling weather stability data
     * @param pollutionTypeDtoMapper the mapper used to transform pollution type entities to DTOs
     * @param vehicleGroupDtoMapper the mapper used to transform vehicle group entities to DTOs
     * @param weatherStabilityDtoMapper the mapper used to transform weather stability entities to DTOs
     */
    @Autowired
    GrassNextController(
            PollutionTypeService pollutionTypeService,
            VehicleGroupService vehicleGroupService,
            WeatherStabilityService weatherStabilityService,
            PollutionTypeDtoMapper pollutionTypeDtoMapper,
            VehicleGroupDtoMapper vehicleGroupDtoMapper,
            WeatherStabilityDtoMapper weatherStabilityDtoMapper
    ) {
        this.pollutionTypeService = pollutionTypeService;
        this.vehicleGroupService = vehicleGroupService;
        this.weatherStabilityService = weatherStabilityService;
        this.pollutionTypeDtoMapper = pollutionTypeDtoMapper;
        this.vehicleGroupDtoMapper = vehicleGroupDtoMapper;
        this.weatherStabilityDtoMapper = weatherStabilityDtoMapper;
    }

    /**
     * Test endpoint for checking the status of the Grass Next server.
     *
     * This method handles an HTTP GET request to the "/start" endpoint and confirms
     * that the server is operational by returning a success message.
     *
     * @return A message string indicating that the Grass Next server is working.
     */
    @GetMapping("start")
    public String grassNextStart() {
        return "Grass Next server is working!";
    }

    /**
     * Retrieves a list of all pollution types available in the system.
     *
     * @return ResponseEntity containing a list of PollutionTypeDto objects representing all
     * pollution types.
     */
    @GetMapping("/pollution-types")
    public ResponseEntity<List<PollutionTypeDto>> getAllPollutionTypes() {
        return ResponseEntity.ok( pollutionTypeDtoMapper.mapAll( pollutionTypeService.getAllPollutionTypes() ) );
    }

    /**
     * Retrieves a list of vehicle types based on the provided detector identifier, date, and time.
     *
     * @param detectorId the unique identifier of the detector for which vehicle group data needs to be retrieved
     * @param date the date for which the data should be retrieved
     * @param time the specific hour (as an integer) for which the data should be retrieved
     * @return ResponseEntity containing a list of VehicleGroupDto objects representing the vehicle types,
     * or a bad request status if the processing fails
     */
    @GetMapping("/vehicle-groups")
    public ResponseEntity<List<VehicleGroupDto>> getAllVehicleTypes(@RequestParam long detectorId, @RequestParam LocalDate date, @RequestParam int time) {
        List<VehicleGroup> vehicleGroupList = vehicleGroupService.getAllVehicleGroups(detectorId, date, time);

        if( vehicleGroupList != null ) {
            return ResponseEntity.ok(vehicleGroupDtoMapper.mapAll(vehicleGroupList));
        } else {
            return ResponseEntity.badRequest().body(null);
        }
    }

    /**
     * Retrieves a list of all weather stabilities available in the system.
     *
     * @return ResponseEntity containing a list of WeatherStabilityDto objects representing all
     * weather stability types.
     */
    @GetMapping("/weather-stabilities")
    public ResponseEntity<List<WeatherStabilityDto>> getAllWeatherStabilities() {
        return ResponseEntity.ok( weatherStabilityDtoMapper.mapAll( weatherStabilityService.getAllWeatherStabilities() ) );
    }
}