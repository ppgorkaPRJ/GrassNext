package com.grassnext.grassnextserver.topodata;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.grassnext.grassnextserver.locationdata.LocationData;
import com.grassnext.grassnextserver.locationdata.LocationDataService;
import com.grassnext.grassnextserver.topodata.topoaggregateddata.TopoAggregatedData;
import com.grassnext.grassnextserver.topodata.topoaggregateddata.TopoAggregatedDataRepository;
import com.grassnext.grassnextserver.topodata.topodetector.TopoDetector;
import com.grassnext.grassnextserver.topodata.topodetector.TopoDetectorRepository;
import com.grassnext.grassnextserver.topodata.topoextradata.TopoExtraData;
import com.grassnext.grassnextserver.topodata.topoheader.TopoHeader;

import java.io.IOException;
import java.io.StringReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

import com.grassnext.grassnextserver.topodata.topomaindata.TopoMainData;
import com.grassnext.grassnextserver.util.Consts;
import com.grassnext.grassnextserver.util.converters.LocalDateTimeConverter;
import com.grassnext.grassnextserver.util.enums.VehicleGroupEnum;
import com.grassnext.grassnextserver.util.gpspoint.GpsPoint;
import com.grassnext.grassnextserver.weather.OpenMeteoService;
import com.grassnext.grassnextserver.weather.Weather;
import com.grassnext.grassnextserver.weather.WeatherRepository;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import jakarta.transaction.Transactional;
import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * The TopoDataParser class is responsible for parsing and processing data read from topo detectors
 * from input files, extracting relevant header, main, extra and remaining time data,
 * and mapping it to appropriate entities for persistence and further processing.
 *
 */
@Service
public class TopoDataParser {
    /**
     * Logger instance for the {@code TopoDataParser} class.
     * Used for logging informational, debug, and error messages related to
     * the configuration and operation of the file watcher within the application.
     *
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(TopoDataParser.class);

    /**
     * Dependency-injected repository used to perform CRUD operations on Weather entities.
     *
     */
    WeatherRepository weatherRepository;
    /**
     * Dependency-injected repository used to perform CRUD operations on TopoDetector entities.
     *
     */
    TopoDetectorRepository topoDetectorRepository;
    /**
     * Dependency-injected repository used to perform CRUD operations on TopoAggregatedData entities.
     *
     */
    TopoAggregatedDataRepository topoAggregatedDataRepository;
    /**
     * Service responsible for managing and retrieving weather data retrieved from OpenMeteo API.
     *
     */
    OpenMeteoService openMeteoService;
    /**
     * Service responsible for managing and retrieving location data retrieved from Overpass API.
     *
     */
    LocationDataService locationDataService;

    /**
     * Represents the header data from a topo file as a string.
     *
     */
    private String headerData;
    /**
     * Represents the main body data from a topo file as a string.
     */
    private String mainData;
    /**
     * Represents the extra data from a topo file as a string.
     */
    private String extraData;
    /**
     * Represents the remaining time data from a topo file as a string.
     */
    private String remainingTimeData;

    /**
     * Represents the header data from a topo file as an object of type {@link TopoHeader}.
     *
     */
    TopoHeader topoHeader;
    /**
     * Represents the main body data from a topo file as a list of objects of type {@link TopoMainData}.
     *
     */
    List<TopoMainData> topoMainData;
    /**
     * Represents the extra data from a topo file as a list of objects of type {@link TopoExtraData}.
     *
     */
    List<TopoExtraData> topoExtraData;

    /**
     * Represents a TopoDetector, which is a component responsible for storing topo detector information.
     *
     */
    TopoDetector topoDetector;
    /**
     * A list containing Weather objects.
     *
     */
    List<Weather> weatherList;

    /**
     * Constructs a new instance of the GrassNextController with the required dependencies for
     * handling pollution types, vehicle groups, and weather stability data.
     *
     * @param weatherRepository the service for managing and processing pollution type data
     * @param topoDetectorRepository the service for managing and processing vehicle group data
     * @param topoAggregatedDataRepository the repository for managing and processing topo aggregated data
     * @param openMeteoService the service for retrieving weather data retrieved from OpenMeteo API
     * @param locationDataService the service for managing and retrieving location data retrieved from Overpass API
     *
     */
    @Autowired
    public TopoDataParser(
            WeatherRepository weatherRepository,
            TopoDetectorRepository topoDetectorRepository,
            TopoAggregatedDataRepository topoAggregatedDataRepository,
            OpenMeteoService openMeteoService,
            LocationDataService locationDataService
                          ) {
        this.weatherRepository = weatherRepository;
        this.topoDetectorRepository = topoDetectorRepository;
        this.topoAggregatedDataRepository = topoAggregatedDataRepository;
        this.openMeteoService = openMeteoService;
        this.locationDataService = locationDataService;
    }

    /**
     * Reads and processes the topo data from the specified file path.
     * The method reads the file content as a string, splits it into segments based on specified delimiters,
     * and extracts specific data components to be assigned to class-level fields.
     *
     * @param fileFullPath The full file path of the topo data file to be read.
     * @throws IOException If an I/O error occurs while accessing or reading the file.
     *
     */
    private void readTopoData(String fileFullPath) throws IOException {
        String topoDataStr = Files.readString(Paths.get(fileFullPath), StandardCharsets.US_ASCII);
        String[] topoDataList = topoDataStr.split("Verkehrsdaten:|Sonstige Daten:|Restlaufzeit:");

        headerData = topoDataList[Consts.TOPO_HEADER_ID].trim();
        mainData = topoDataList[Consts.TOPO_MAIN_DATA_ID].trim().replace(";", "");
        extraData = topoDataList[Consts.TOPO_EXTRA_DATA_ID].trim().replace(";", "");
        remainingTimeData = topoDataList[Consts.TOPO_REMAINING_TIME_ID].trim().replace(";", "");
    }

    /**
     * Parses the TopoHeader data from a string and populates a {@code TopoHeader} object with the extracted and processed values.
     *
     * @throws IOException if there is an error during the header properties loading process or string processing.
     *
     */
    private void parseTopoHeader() throws IOException {
        List<String> headerList = List.of( headerData.split(";") );

        String headerStr = headerList.stream()
                .map(line -> {
                    String[] split = line.split("=");
                    return split[0].replace(" ", "_") + "=" + split[1];
                })
                .collect(Collectors.joining(System.lineSeparator()));

        Properties headerProps = new Properties();
        headerProps.load( new StringReader(headerStr) );

        LocalDateTimeConverter ldtc = new LocalDateTimeConverter();
        topoHeader = new TopoHeader();
        try {
            PropertyUtils.setProperty( topoHeader, "producerCode", headerProps.getProperty("Herstellercode") );
            PropertyUtils.setProperty( topoHeader, "deviceId", headerProps.getProperty("Geraete_ID") );
            PropertyUtils.setProperty( topoHeader, "classificationCode", Integer.parseInt( headerProps.getProperty("Klassifizierungscode") ) );
            PropertyUtils.setProperty( topoHeader, "rn", headerProps.getProperty("Rn") );
            PropertyUtils.setProperty( topoHeader, "rf", headerProps.getProperty("Rf") );
            PropertyUtils.setProperty( topoHeader, "startTime", ldtc.convert( headerProps.getProperty("Beginn") ) );
            PropertyUtils.setProperty( topoHeader, "endTime", ldtc.convert( headerProps.getProperty("Ende") ) );
            PropertyUtils.setProperty( topoHeader, "hardwareVersion", headerProps.getProperty("Hardwareversion") );
            PropertyUtils.setProperty( topoHeader, "exportStartTime", ldtc.convert( headerProps.getProperty("Exportbeginn") ) );
            PropertyUtils.setProperty( topoHeader, "exportEndTime", ldtc.convert( headerProps.getProperty("Exportende") ) );
            PropertyUtils.setProperty( topoHeader, "exportFormat", headerProps.getProperty("Exportformat") );

            PropertyUtils.setProperty( topoHeader, "measurementNumber", Integer.parseInt( headerProps.getProperty("Messung_Nr.") ) );
            PropertyUtils.setProperty( topoHeader, "measurementStartTime", ldtc.convert( headerProps.getProperty("Mess_Beginn") ) );
            PropertyUtils.setProperty( topoHeader, "measurementEndTime", ldtc.convert( headerProps.getProperty("Mess_Ende") ) );

            PropertyUtils.setProperty( topoHeader, "gpsLongitude", BigDecimal.valueOf(Double.parseDouble(headerProps.getProperty("GPS_long").replace(",", "."))).setScale(4, RoundingMode.HALF_UP).doubleValue() );
            PropertyUtils.setProperty( topoHeader, "gpsLatitude", BigDecimal.valueOf(Double.parseDouble(headerProps.getProperty("GPS_lat").replace(",", "."))).setScale(4, RoundingMode.HALF_UP).doubleValue() );

            PropertyUtils.setProperty( topoHeader, "gpsDateTime", ldtc.convert( headerProps.getProperty("GPS_Uhrzeit") ) );
            PropertyUtils.setProperty( topoHeader, "gpsQuality", Integer.parseInt( headerProps.getProperty("GPS_Qualitaet") ) );
            PropertyUtils.setProperty( topoHeader, "gpsSattelites", Integer.parseInt( headerProps.getProperty("GPS_Satelliten") ) );
            PropertyUtils.setProperty( topoHeader, "gpsHdop", Double.parseDouble( headerProps.getProperty("GPS_HDOP").replace(",", ".")) );
            PropertyUtils.setProperty( topoHeader, "softwareVersion", headerProps.getProperty("Softwareversion") );
            PropertyUtils.setProperty( topoHeader, "remainingTime", Integer.parseInt( remainingTimeData ) );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates a TopoDetector object by either retrieving an existing matching record from the repository
     * or creating and saving a new one.
     *
     * @throws JsonProcessingException if there is an error in processing JSON data while collecting location information.
     *
     */
    private void createTopoDetector() throws JsonProcessingException {
        topoDetector = topoDetectorRepository.findExistingTopoDetector(topoHeader.getGpsLongitude(), topoHeader.getGpsLatitude(), topoHeader.getDeviceId());
        if( topoDetector == null ) {
            LocationData ld = locationDataService.getOverpassNodesData(topoHeader.getGpsLongitude(), topoHeader.getGpsLatitude(), topoHeader.getDeviceId());

            // TODO: Sprawdzić lokację z bazą

            topoDetector = new TopoDetector(
                    topoHeader.getDeviceId(),
                    ld,
                    new GpsPoint(
                            topoHeader.getGpsLongitude(),
                            topoHeader.getGpsLatitude()
                    )
            );

            topoDetectorRepository.save(topoDetector);
        }
    }

    /**
     * Parses the topo main data from a string in CSV format.
     *
     **/
    private void parseTopoMainData() {
        StringReader stringReader = new StringReader(mainData);
        CsvToBean<TopoMainData> csvMainData = new CsvToBeanBuilder<TopoMainData>(stringReader)
                .withType(TopoMainData.class)
                .withSeparator('\t')
                .build();
        topoMainData = csvMainData.parse();

        topoMainData.forEach(tmd -> {
            tmd.setDateTime( tmd.getDate(), tmd.getTime() );
            tmd.setVehicleGroup( switch (tmd.getVehicleCode()) {
                case MOTORCYCLE -> VehicleGroupEnum.MOTORCYCLE_GROUP;
                case PASSENGER_CAR, CAR_WITH_TRAILER -> VehicleGroupEnum.PASSENGER_CAR_GROUP;
                case SMALL_TRUCK -> VehicleGroupEnum.LIGHT_COMMERCIAL_VEHICLE_GROUP;
                case TRUCK, BIG_TRUCK, TRUCK_WITH_TRAILER, TRACTOR_TRAILER_TRUCK -> VehicleGroupEnum.HEAVY_GOODS_VEHICLE_GROUP;
                case BUS -> VehicleGroupEnum.PUBLIC_TRANSPORT_BUS_GROUP;
                default -> VehicleGroupEnum.NOT_CLASSIFIED;
            });
        });
    }

    /**
     * Aggregates and processes traffic data from individual `TopoMainData` entries into a summarized format
     * and stores the resulting aggregated data in the database.
     *
     */
    private void aggregateTopoMainData() {
        int[][] vehilceSum = new int[Consts.TOPO_HOURS][VehicleGroupEnum.values().length];
        double[][] velocityAvgSum = new double[Consts.TOPO_HOURS][VehicleGroupEnum.values().length];

        for (TopoMainData tmd : topoMainData) {
            vehilceSum[tmd.getDateTime().getHour()][tmd.getVehicleGroup().get()]++;
            velocityAvgSum[tmd.getDateTime().getHour()][tmd.getVehicleGroup().get()] += tmd.getVelocity();
        }

        List<TopoAggregatedData> topoAggregatedData = new ArrayList<>();
        for( int i = 0; i < Consts.TOPO_HOURS; i++ ) {
            for (int j = 0; j < VehicleGroupEnum.values().length; j++) {
                topoAggregatedData.add(new TopoAggregatedData(
                        topoDetector,
                        topoHeader.getExportStartTime().toLocalDate(),
                        i + 1,
                        VehicleGroupEnum.getByValue(j),
                        vehilceSum[i][j],
                        (vehilceSum[i][j] > 0.0) ? velocityAvgSum[i][j]/vehilceSum[i][j] : 0.0
                ));
            }
        }

        topoAggregatedDataRepository.saveAll(topoAggregatedData);
    }

    /**
     * Parses the topo extra data from a string in CSV format.
     *
     **/
    private void parseTopoExtraData() {
        StringReader stringReader = new StringReader(extraData);
        CsvToBean<TopoExtraData> csvExtraData = new CsvToBeanBuilder<TopoExtraData>(stringReader)
                .withType(TopoExtraData.class)
                .withSeparator('\t')
                .build();
        topoExtraData = csvExtraData.parse();

        topoExtraData.forEach(ted -> ted.setDateTime( ted.getDate(), ted.getTime() ));
    }

    /**
     * Parses weather data retrieved from the OpenMeteo service and saves it to the repository.
     *
     * @throws JsonProcessingException if there is an error in processing the JSON response
     *                                 from the OpenMeteo service.
     */
    private void parseOpenMeteoWeather() throws JsonProcessingException {
        weatherList = openMeteoService.getWindSpeedAndDirection(
                topoHeader.getGpsLongitude(),
                topoHeader.getGpsLatitude(),
                topoHeader.getExportStartTime(),
                topoHeader.getExportEndTime()
        );

        weatherRepository.saveAll(weatherList);
    }

    /**
     * Imports and processes topo data from the specified file.
     *
     * @param fullFilePath the full file path to the topology data file
     * @return true if the data import and processing are successful
     * @throws IOException if an I/O error occurs while reading the file
     */
    @Transactional
    public boolean importTopoData(String fullFilePath) throws IOException {
        readTopoData(fullFilePath);
        parseTopoHeader();
        createTopoDetector();
        parseTopoMainData();
        aggregateTopoMainData();
        parseTopoExtraData();
        parseOpenMeteoWeather();

        return true;
    }
}
