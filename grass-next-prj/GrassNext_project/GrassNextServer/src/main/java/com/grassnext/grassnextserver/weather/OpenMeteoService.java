package com.grassnext.grassnextserver.weather;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.grassnext.grassnextserver.util.Consts.DATE_FORMATTER_WEATHER;

/**
 * The OpenMeteoService class interacts with the Open-Meteo API to fetch and process weather data.
 * It implements the WeatherInterface to provide weather forecasting functionalities.
 * This service supports fetching wind speed and wind direction for specified locations and time periods
 * and fetching saved weather data from the database.
 */
@Service
@Data
@NoArgsConstructor
public class OpenMeteoService implements WeatherInterface {
    /**
     * Represents the base URL for accessing the Open-Meteo API's weather forecasting services.
     *
     */
    public static String HOST_OPEN_METEO = "https://api.open-meteo.com/v1/forecast";
    /**
     * Represents the maximum number of hour entries (from midnight) to fetch from the Open-Meteo API..
     *
     */
    public static int MAX_HOURS = 25;

    /**
     * Represents the JSON structure for weather data fetched from the Open-Meteo API.
     *
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class WeatherDataJson {
        /**
         * Data from OpenMeto API
         *
         */
        public static class Hourly {
            /**
             * Weather measurement time
             *
             */
            public ArrayList<LocalDateTime> time;
            /**
             * Wind speed in kilometers per hour
             *
             */
            public ArrayList<Double> windspeed_10m;
            /**
             * Wind direction in degrees
             *
             */
            public ArrayList<Integer> winddirection_10m;
        }

        /**
         * Instance of {@link Hourly} class.
         */
        public Hourly hourly;
    }

    /**
     * Represents an instance of the Jackson {@link ObjectMapper} configured with the {@link JavaTimeModule}
     * to handle deserialization of Weather from JSON.
     *
     */
    ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    /**
     * Dependency-injected repository used to perform CRUD operations on Weather entities.
     *
     */
    WeatherRepository weatherRepository;

    /**
     * Constructs a new instance of OpenMeteoService.
     *
     * @param weatherRepository the repository used for CRUD operations on {@link WeatherRepository} entities
     *
     */
    @Autowired
    public OpenMeteoService(WeatherRepository weatherRepository) {
        this.weatherRepository = weatherRepository;
    }

    /**
     * Retrieves a list of weather data containing wind speed and wind direction for a specific geographical location
     * and time range by calling the Open-Meteo API.
     *
     * @param longitude the geographical longitude of the location
     * @param latitude the geographical latitude of the location
     * @param startDate the starting date and time of the required weather data in LocalDateTime format
     * @param endDate the ending date and time of the required weather data in LocalDateTime format
     * @return a list of {@link Weather} objects containing wind speed and wind direction for each hour in the specified time range
     * @throws JsonProcessingException if an error occurs during JSON processing while deserializing the API response
     */
    public List<Weather> getWindSpeedAndDirection(double longitude, double latitude, LocalDateTime startDate, LocalDateTime endDate) throws JsonProcessingException {
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromHttpUrl(HOST_OPEN_METEO)
                .queryParam("latitude", String.valueOf(latitude))
                .queryParam("longitude", String.valueOf(longitude))
                .queryParam("hourly", "windspeed_10m,winddirection_10m")
                .queryParam("timezone", "auto")
                .queryParam("start_date", startDate.format(DATE_FORMATTER_WEATHER))
                .queryParam("end_date", endDate.plusDays(1).format(DATE_FORMATTER_WEATHER))
                .queryParam("windspeed_unit", "ms");

        final String uri = uriComponentsBuilder.build().toString();

        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.getForObject(uri, String.class);

        WeatherDataJson weatherData = objectMapper.readValue(result, WeatherDataJson.class);

        List<Weather> weatherList = new ArrayList<>();
        for(int i = 1; i < MAX_HOURS; i++) {
            WeatherDataJson.Hourly wh = weatherData.hourly;

            weatherList.add( new Weather(
                    startDate.toLocalDate(),
                    i % MAX_HOURS,
                    longitude,
                    latitude,
                    wh.windspeed_10m.get(i),
                    wh.winddirection_10m.get(i)
            ));
        }

        return weatherList;
    }

    /**
     * Retrieves the weather data for a specific date, hour, and geographical location.
     *
     * @param measurementDate the date for which the weather data is requested
     * @param measurementHour the specific hour of the day for which the weather data is requested
     * @param longitude the geographical longitude of the location
     * @param latitude the geographical latitude of the location
     * @return the {@link Weather} object containing the weather data for the requested date, hour, and location,
     *         or null if no matching weather data is found
     */
    public Weather getWeatherByLocationAndDate(LocalDate measurementDate, int measurementHour, double longitude, double latitude) {
        return weatherRepository.findWeatherByMeasurementDateAndMeasurementHourAndLongitudeAndLatitude(measurementDate, measurementHour, longitude, latitude);
    }
}
