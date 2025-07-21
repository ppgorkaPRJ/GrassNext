package com.grassnext.grassnextserver.locationdata;

import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * The LocationDataController class provides RESTful endpoints for retrieving location-related data.
 * It allows fetching lists of countries, states, and cities based on the input parameters.
 *
 * The controller is accessed via "/api/location/" base URI and handles the following endpoints:
 * - GET /countries: Retrieves the list of available countries.
 * - GET /states: Retrieves the list of states within a specified country.
 * - GET /cities: Retrieves the list of cities within a specified state and country.
 *
 * The controller depends on LocationDataService to fetch the required data.
 */
@RestController
@RequestMapping("/api/location/")
@NoArgsConstructor
public class LocationDataController {
    /**
     * Service instance for managing location data-related operations.
     *
     */
    private LocationDataService locationDataService;

    /**
     * Constructs a new instance of the LocationDataController with the required dependencies.
     *
     * @param locationDataService the service for managing and processing location related data
     */
    @Autowired
    public LocationDataController(
            LocationDataService locationDataService
    ) {
        this.locationDataService = locationDataService;
    }

    /**
     * Retrieves a list of all countries available in the system.
     *
     * @return ResponseEntity containing a list of countries
     *
     */
    @GetMapping("/countries")
    public ResponseEntity<List<String>> getCountryList() {
        return ResponseEntity.ok( locationDataService.getCountryList() );
    }

    /**
     * Retrieves a list of all states available in the system.
     *
     * @return ResponseEntity containing a list of states
     *
     */
    @GetMapping("/states")
    public ResponseEntity<List<String>> getStateList(@RequestParam("country") String country) {
        return ResponseEntity.ok( locationDataService.getStateList(country) );
    }

    /**
     * Retrieves a list of all cities available in the system.
     *
     * @return ResponseEntity containing a list of cities
     *
     */
    @GetMapping("/cities")
    public ResponseEntity<List<String>> getTownList(@RequestParam("country") String country, @RequestParam("state") String state) {
        return ResponseEntity.ok( locationDataService.getTownList(country, state) );
    }
}
