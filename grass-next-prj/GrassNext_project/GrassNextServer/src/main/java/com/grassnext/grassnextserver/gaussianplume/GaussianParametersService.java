package com.grassnext.grassnextserver.gaussianplume;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.grassnext.grassnextserver.common.MeasurementData;
import com.grassnext.grassnextserver.locationdata.LocationData;
import com.grassnext.grassnextserver.topodata.topoaggregateddata.TopoAggregatedData;
import com.grassnext.grassnextserver.topodata.topoaggregateddata.TopoAggregatedDataRepository;
import com.grassnext.grassnextserver.topodata.topodetector.TopoDetector;
import com.grassnext.grassnextserver.topodata.topodetector.TopoDetectorRepository;
import com.grassnext.grassnextserver.util.Consts;
import com.grassnext.grassnextserver.weather.Weather;
import com.grassnext.grassnextserver.weather.WeatherRepository;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Service class responsible for managing Gaussian plume parameters and generating
 * the Gaussian plume model based on environmental and measurement data.
 *
 */
@Data
@Service
@NoArgsConstructor
public class GaussianParametersService {
    /**
     * A static instance of the Jackson {@code ObjectMapper} used for serializing
     * and deserializing objects to and from JSON strings.
     *
     */
    static ObjectMapper jsonMapper = new ObjectMapper();

    /**
     * Dependency-injected repository used to perform CRUD operations on TopoDetector entities.
     *
     */
    TopoDetectorRepository topoDetectorRepository;
    /**
     * Dependency-injected repository used to perform CRUD operations on Weather entities.
     *
     */
    WeatherRepository weatherRepository;
    /**
     * Dependency-injected repository used to perform CRUD operations on TopoAggregatedData entities.
     *
     */
    TopoAggregatedDataRepository topoAggregatedDataRepository;

    /**
     * Constructs a GaussianParametersService instance and initializes the required repositories.
     *
     * @param topoDetectorRepository the repository for accessing topology detector data
     * @param weatherRepository the repository for accessing weather data
     * @param topoAggregatedDataRepository the repository for accessing the aggregated topo data
     *
     */
    @Autowired
    public GaussianParametersService(
            TopoDetectorRepository topoDetectorRepository,
            WeatherRepository weatherRepository,
            TopoAggregatedDataRepository topoAggregatedDataRepository
    ) {
        this.topoDetectorRepository = topoDetectorRepository;
        this.weatherRepository = weatherRepository;
        this.topoAggregatedDataRepository = topoAggregatedDataRepository;
    }

    /**
     * Generates a Gaussian Plume model for air pollution dispersion calculation
     * based on the provided measurement data, geographical data, weather conditions,
     * and vehicle traffic information.
     *
     * @param measurementData the data object containing measurement-related information including
     *                        detector ID, date, time, selected vehicles, pollution type,
     *                        weather stability, and area specifications
     * @return a JSON string representing the constructed Gaussian Plume model including details
     *         about wind parameters, road geometry, vehicle information, and pollution thresholds
     * @throws JsonProcessingException if the Gaussian Plume object cannot be serialized into JSON
     */
    public String getGaussianPlume(MeasurementData measurementData) throws JsonProcessingException {
        TopoDetector topoDetector = topoDetectorRepository.findTopoDetectorById(measurementData.getDetectorId());
        if(topoDetector == null) {
            return Consts.INCORRECT_DATA_HEADER + "Detector not found!";
        }
        LocationData locationData = topoDetector.getLocationData();

        Weather weather = weatherRepository.findWeatherByMeasurementDateAndMeasurementHourAndLongitudeAndLatitude(
                measurementData.getDate(),
                measurementData.getTime(),
                topoDetector.getTopoLocation().getLongitude(),
                topoDetector.getTopoLocation().getLatitude()
        );
        if(weather == null) {
            return Consts.INCORRECT_DATA_HEADER + "No weather data found for chosen date!";
        }

        List<TopoAggregatedData> topoData = topoAggregatedDataRepository.findTopoAggregatedDataList(
                topoDetector,
                measurementData.getDate(),
                measurementData.getTime()
        );
        if(topoData == null) {
            return Consts.INCORRECT_DATA_HEADER + "No vehicle data found for chosen date!";
        }

        List<GaussianPlume.Vehicle> vehicleList = new ArrayList<>();
        for(int v = 0; v < topoData.size(); v++) {
            TopoAggregatedData topoVehicle = topoData.get(v);
            vehicleList.add( new GaussianPlume.Vehicle(false, topoVehicle.getVehicleCountHour(),topoVehicle.getVehicleAvgVelocityHour()) );
        }

        for(int vehicleIdx : measurementData.getVehicles()) {
            vehicleList.get(vehicleIdx - 1).setChosen(true);
        }

        // Latitude:  53° 24' 59.99" N
        // Longitude: 14° 34' 59.99" E
        GaussianPlume gaussianPlume = GaussianPlume.GaussianPlumeBuilder()
                .vehicles(vehicleList)
                .wind(new GaussianPlume.Wind(weather.getWindSpeed(), weather.getWindDirection(), measurementData.getWeatherStability()))
                .roadStart(new GaussianPlume.GpsPoint(locationData.getStartNode().getLongitude(), locationData.getStartNode().getLatitude()))
                .roadEnd(new GaussianPlume.GpsPoint(locationData.getEndNode().getLongitude(), locationData.getEndNode().getLatitude()))
                .thresholds(Consts.MAX_THRESHOLDS)
                .pollutionType(measurementData.getPollutionType())
                .emittersHeight(Consts.EMITTERS_HEIGHT)
                .concentrationHeight(Consts.CONCENTRATION_HEIGHT)
                .matrixSize(measurementData.getArea())
                .cellResolution(Consts.CELL_RESOLUTION)
                .divMatrixSide(Consts.DIVISOR_MATRIX_SIDE)
                .build();

        return jsonMapper.writeValueAsString(gaussianPlume);
    }
}
