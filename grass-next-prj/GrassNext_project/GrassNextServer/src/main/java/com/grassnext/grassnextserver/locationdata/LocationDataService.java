package com.grassnext.grassnextserver.locationdata;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.grassnext.grassnextserver.util.gpspoint.GpsPoint;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Service class responsible for managing REST operations related to LocationData entities.
 *
 */
@Service
@Data
@NoArgsConstructor
public class LocationDataService {
    /**
     * A constant string representing the URL endpoint for reverse geocoding
     * using the Nominatim OpenStreetMap API. This endpoint is used to retrieve
     * geographic information based on coordinate data.
     */
    public static String HOST_REVERSE_GEOCODING = "https://nominatim.openstreetmap.org/reverse";
    /**
     * A constant that holds the URL endpoint for the Overpass API interpreter.
     * This URL is used to interact with OpenStreetMap (OSM) location data by sending
     * queries to the Overpass API.
     */
    public static String HOST_OSM_LOCATION_DATA = "http://overpass-api.de/api/interpreter";

    /**
     * A class representing location data in JSON format, designed for deserialization
     * of location details. This class and its nested classes ignore unknown properties
     * during JSON processing.
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class LocationDataJson {
        /**
         * Represents an address with various levels of geographical information.
         * This class is configured to ignore unknown properties during JSON deserialization
         */
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Address {
            /**
             * Represents the name or identifier of a road.
             *
             */
            public String road;
            /**
             * Represents the municipality associated with a specific entity.
             *
             */
            public String municipality;
            /**
             * Represents the name of a city.
             *
             */
            public String city;
            /**
             * Represents the name of a town.
             *
             */
            public String town;
            /**
             * Represents the name of a village.
             *
             */
            public String village;
            /**
             * Represents the state.
             *
             */
            public String state;
            /**
             * Represents the name of a country.
             *
             */
            public String country;
        }

    /**
     * A variable that stores an Address object
     *
     */
        public Address address;
    }

    /**
     * This class represents a JSON structure used to manage the deserialization of a list of nodes.
     * It is designed to parse and store geospatial information for a collection of nodes.
     *
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class NodeListJson {
        /**
         * Represents a geographical element with latitude and longitude coordinates
         * as well as a unique identifier. It is designed to map or store basic geographical
         * data. This class is configured to ignore unknown properties during JSON deserialization.
         *
         */
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Element {
            /**
             * Represents the unique identifier for an entity.
             *
             */
            public Long id;
            /**
             * Represents the latitude component of a geographical coordinate.
             *
             */
            public double lat;
            /**
             * Represents the longitude coordinate of a geographical location.
             *
             */
            public double lon;
        }
        /**
         * A list that holds multiple Element objects.
         * Used to store and manage a collection of Element instances in the form of an ArrayList.
         *
         */
        public ArrayList<Element> elements;
    }

    /**
     * Dependency-injected repository used to perform CRUD operations on LocationData entities.
     *
     */
    LocationDataRepository locationDataRepository;

    /**
     * Constructs a new instance of LocationDataService.
     *
     * @param locationDataRepository the repository used for CRUD operations on {@link LocationData} entities
     *
     */
    @Autowired
    LocationDataService(LocationDataRepository locationDataRepository) {
        this.locationDataRepository = locationDataRepository;
    }

    /**
     * ObjectMapper is an instance of the Jackson library's ObjectMapper class.
     * This class is used to map Java objects to JSON and JSON to Java objects.
     *
     */
    ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Fetches location data including nearby nodes by querying Overpass API
     * based on the given longitude and latitude. The method attempts to find
     * the closest nodes that match the required criteria and saves the data
     * in the repository.
     *
     * @param longitude the longitude of the location for the query
     * @param latitude the latitude of the location for the query
     * @param deviceId the unique identifier of the topo device
     * @return a LocationData object containing details such as location name,
     *         start and end nodes, and nearby road node IDs; or null if no
     *         sufficient data is retrieved
     * @throws JsonProcessingException if there's an error during the processing
     *         of JSON data
     */
    public LocationData getOverpassNodesData(double longitude, double latitude, String deviceId) throws JsonProcessingException {
        int key = 2;
        int low = 0;
        int high = 1000;

        LocationData ld = new LocationData();
        ld.setLongitude(longitude);
        ld.setLatitude(latitude);
        ld.setDeviceId(deviceId);

        ld.setLocationName( getLocationName(ld) );

        while( low <= high ) {
            int mid = low + ( (high - low) / 2 );

            String query = String.format(Locale.US,"""
                    [out:json];
                    way(around:%d, %f, %f)["highway"~"motorway|trunk|primary|secondary|tertiary|residential|service|living_street|unclassified"];
                    node(w:1,-1);
                    out geom;""", mid, latitude, longitude);

            UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromHttpUrl(HOST_OSM_LOCATION_DATA)
                    .queryParam("data", query);

            final String uri = uriComponentsBuilder.build().toString();

            RestTemplate restTemplate = new RestTemplate();
            String result = restTemplate.getForObject(uri, String.class);

            LocationDataService.NodeListJson nodeListJson = objectMapper.readValue(result, LocationDataService.NodeListJson.class);

            if(nodeListJson.elements.size() < key) {
                low = mid + 1;
            } else if (nodeListJson.elements.size() > key) {
                high = mid - 1;
            } else {
                ld.setStartNode( new GpsPoint(nodeListJson.elements.get(0).lon, nodeListJson.elements.get(0).lat) );
                ld.setEndNode( new GpsPoint(nodeListJson.elements.get(1).lon, nodeListJson.elements.get(1).lat) );

                List<Long> roadNodeIds = new ArrayList<>();
                roadNodeIds.add( nodeListJson.elements.get(0).id );
                roadNodeIds.add( nodeListJson.elements.get(1).id );
                ld.setNodeIds(roadNodeIds);

                locationDataRepository.save(ld);

                return ld;
            }
        }

        return null;
    }

    /**
     * Retrieves and constructs the location name based on the provided geographic coordinates
     * encapsulated in the LocationData object by making use of a reverse geocoding API.
     * Updates the location details of the LocationData object and builds a string
     * representation of the location.
     *
     * @param ld the LocationData object containing latitude, longitude, and device ID information,
     *           which will also be updated with additional location details such as road, town,
     *           state, and country
     * @return a string representing the formatted location information, including the device ID
     *         and geographic coordinates
     * @throws JsonProcessingException if an error occurs while processing the JSON response from
     *                                 the reverse geocoding API
     */
    private String getLocationName(LocationData ld) throws JsonProcessingException {
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromHttpUrl(HOST_REVERSE_GEOCODING)
                .queryParam("format", "json")
                .queryParam("lat", String.valueOf(ld.getLatitude()))
                .queryParam("lon", String.valueOf(ld.getLongitude()))
                .queryParam("zoom", "18")
                .queryParam("addressdetails", "1")
                .queryParam("accept-language", "en");

        final String uri = uriComponentsBuilder.build().toString();

        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.getForObject(uri, String.class);

        LocationDataService.LocationDataJson locationData = objectMapper.readValue(result, LocationDataService.LocationDataJson.class);
        ld.setRoad(locationData.address.road);

        if (locationData.address.municipality != null) {
            ld.setTown(locationData.address.municipality);
        } else if (locationData.address.city != null) {
            ld.setTown(locationData.address.city);
        } else if (locationData.address.town != null) {
            ld.setTown(locationData.address.town);
        } else if (locationData.address.village != null) {
            ld.setTown(locationData.address.village);
        } else {
            ld.setTown("Not specified");
        }

        ld.setState(locationData.address.state);
        ld.setCountry(locationData.address.country);

        StringBuilder sb = new StringBuilder();
        sb.append(" #");
        sb.append(ld.getDeviceId());
        sb.append(" (");
        sb.append(ld.getLatitude());
        sb.append(", ");
        sb.append(ld.getLongitude());
        sb.append(")");

        return sb.toString();
    }

    /**
     * Retrieves a list of countries from the data repository.
     *
     * @return a list of country names
     *
     */
    public List<String> getCountryList() {
        return locationDataRepository.getCountries();
    }

    /**
     * Retrieves a list of states for the specified country from the repository.
     *
     * @param country the name of the country for which the states are to be retrieved
     * @return a list of state names within the specified country
     *
     */
    public List<String> getStateList(String country) {
        return locationDataRepository.getStates(country);
    }

    /**
     * Retrieves a list of towns based on the specified country and state.
     *
     * @param country the name of the country to filter towns
     * @param state the name of the state to filter towns
     * @return a list of towns that belong to the specified country and state
     *
     */
    public List<String> getTownList(String country, String state) {
        return locationDataRepository.getTowns(country, state);
    }
}