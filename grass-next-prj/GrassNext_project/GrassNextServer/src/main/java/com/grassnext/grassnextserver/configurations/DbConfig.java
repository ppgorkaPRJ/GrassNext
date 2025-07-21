package com.grassnext.grassnextserver.configurations;

import com.grassnext.grassnextserver.common.pollutiontype.PollutionType;
import com.grassnext.grassnextserver.common.pollutiontype.PollutionTypeRepository;
import com.grassnext.grassnextserver.common.vehiclegroup.VehicleGroup;
import com.grassnext.grassnextserver.common.vehiclegroup.VehicleGroupRepository;
import com.grassnext.grassnextserver.common.weatherstability.WeatherStability;
import com.grassnext.grassnextserver.common.weatherstability.WeatherStabilityRepository;
import lombok.Data;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * Configuration class for initializing the database with predefined data for PollutionType, VehicleGroup,
 * and WeatherStability entities.
 *
 */
@Configuration
@Data
public class DbConfig implements InitializingBean {
    /**
     * Dependency-injected repository used to perform CRUD operations on PollutionType entities.
     *
     */
    PollutionTypeRepository pollutionTypeRepository;
    /**
     * Dependency-injected repository used to perform CRUD operations on VehicleGroup entities.
     *
     */
    VehicleGroupRepository vehicleGroupRepository;
    /**
     * Dependency-injected repository used to perform CRUD operations on WeatherStability entities.
     *
     */
    WeatherStabilityRepository weatherStabilityRepository;

    /**
     * Constructs a new instance of the DbConfig class.
     *
     * @param pollutionTypeRepository the repository used for CRUD operations on PollutionType entities
     * @param vehicleGroupRepository the repository used for CRUD operations on VehicleGroup entities
     * @param weatherStabilityRepository the repository used for CRUD operations on WeatherStability entities
     */
    DbConfig(
            PollutionTypeRepository pollutionTypeRepository,
            VehicleGroupRepository vehicleGroupRepository,
            WeatherStabilityRepository weatherStabilityRepository
    ) {
        this.pollutionTypeRepository = pollutionTypeRepository;
        this.vehicleGroupRepository = vehicleGroupRepository;
        this.weatherStabilityRepository = weatherStabilityRepository;
    }

    /**
     * Initializes the bean after its properties have been set by the Spring container.
     *
     */
    @Override
    public void afterPropertiesSet() {
        pollutionTypeRepository.saveAll(pollutionTypeList);
        vehicleGroupRepository.saveAll(vehicleGroupList);
        weatherStabilityRepository.saveAll(weatherStabilityList);
    }

    /**
     * A static list containing instances of the {@link PollutionType} class.
     *
     */
    static List<PollutionType> pollutionTypeList = new ArrayList<>();
    static {
        pollutionTypeList.add(new PollutionType(1L, "CO&nbsp(carbon monoxide)", "CO"));
        pollutionTypeList.add(new PollutionType(2L, "NO<sub>x</sub>&nbsp(nitrogen oxides)", "NOx"));
        pollutionTypeList.add(new PollutionType(3L, "CH&nbsp(hydrocarbons)", "CH"));
    }

    /**
     * A static list containing instances of the {@link VehicleGroup} class.
     *
     */
    static List<VehicleGroup> vehicleGroupList = new ArrayList<>();
    static {
        vehicleGroupList.add(new VehicleGroup(1L, "<span class=\"material-symbols-outlined\"> two_wheeler </span>&nbspMotorcycles", "Motorcycles"));
        vehicleGroupList.add(new VehicleGroup(2L, "<span class=\"material-symbols-outlined\"> directions_car </span>&nbspPassenger cars", "Passenger cars"));
        vehicleGroupList.add(new VehicleGroup(3L, "<span class=\"material-symbols-outlined\"> airport_shuttle </span>&nbspLight commercial vehicles", "Light commercial vehicles"));
        vehicleGroupList.add(new VehicleGroup(4L, "<span class=\"material-symbols-outlined\"> local_shipping </span>&nbspHeavy goods vehicles", "Heavy goods vehicles"));
        vehicleGroupList.add(new VehicleGroup(5L, "<span class=\"material-symbols-outlined\"> directions_bus </span>&nbspPublic transport buses", "Public transport buses"));
    }

    /**
     * A static list containing instances of the {@link WeatherStability} class.
     *
     */
    static List<WeatherStability> weatherStabilityList = new ArrayList<>();
    static {
        weatherStabilityList.add(new WeatherStability(1L, "Extremely unstable"));
        weatherStabilityList.add(new WeatherStability(2L, "Moderately unstable"));
        weatherStabilityList.add(new WeatherStability(3L, "Slightly unstable"));
        weatherStabilityList.add(new WeatherStability(4L, "Neutral"));
        weatherStabilityList.add(new WeatherStability(5L, "Slightly stable"));
        weatherStabilityList.add(new WeatherStability(6L, "Moderately stable"));
    }
}
