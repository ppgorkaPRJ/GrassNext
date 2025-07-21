package com.grassnext.grassnextserver.common.vehiclegroup;

import com.grassnext.grassnextserver.topodata.topoaggregateddata.TopoAggregatedData;
import com.grassnext.grassnextserver.topodata.topoaggregateddata.TopoAggregatedDataRepository;
import com.grassnext.grassnextserver.topodata.topodetector.TopoDetector;
import com.grassnext.grassnextserver.topodata.topodetector.TopoDetectorRepository;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

/**
 * Service class responsible for managing REST operations related to VehicleGroup entities.
 *
 */
@Service
@Data
@NoArgsConstructor
public class VehicleGroupService {
    /**
     * Dependency-injected repository used to perform CRUD operations on PollutionType entities.
     *
     */
    VehicleGroupRepository vehicleGroupRepository;
    /**
     * Dependency-injected repository used to perform CRUD operations on PollutionType entities.
     *
     */
    TopoDetectorRepository topoDetectorRepository;
    /**
     * Dependency-injected repository used to perform CRUD operations on PollutionType entities.
     *
     */
    TopoAggregatedDataRepository topoAggregatedDataRepository;

    /**
     * Constructs a new instance of VehicleGroupService.
     *
     * @param vehicleGroupRepository the repository used for CRUD operations on {@link VehicleGroupRepository} entities
     * @param topoDetectorRepository the repository used for CRUD operations on {@link TopoDetectorRepository} entities
     * @param topoAggregatedDataRepository the repository used for CRUD operations on {@link TopoAggregatedDataRepository} entities
     */
    @Autowired
    VehicleGroupService(
            VehicleGroupRepository vehicleGroupRepository,
            TopoDetectorRepository topoDetectorRepository,
            TopoAggregatedDataRepository topoAggregatedDataRepository
    ) {
        this.vehicleGroupRepository = vehicleGroupRepository;
        this.topoDetectorRepository = topoDetectorRepository;
        this.topoAggregatedDataRepository = topoAggregatedDataRepository;
    }

    /**
     * Retrieves a list of all vehicle groups with vehicle count data based on the specified detector ID,
     * date, and time. If the detector cannot be found or if associated data is not available, this method returns null.
     *
     * @param detectorId the ID of the topo detector for which vehicle count data should be retrieved
     * @param date the specific date used to filter the topo aggregated data
     * @param time the specific hour of the day used to filter the topo aggregated data
     * @return a list of {@link VehicleGroup} entities with vehicle count information,
     *         or null if the detector is not found or no data is available for the given parameters
     */
    public List<VehicleGroup> getAllVehicleGroups(long detectorId, LocalDate date, int time) {
        List<VehicleGroup> vehicleGroupList = vehicleGroupRepository.findAll();

        TopoDetector topoDetector = topoDetectorRepository.findTopoDetectorById(detectorId);
        if(topoDetector == null) {
            return null;
        }

        List<TopoAggregatedData> topoData = topoAggregatedDataRepository.findTopoAggregatedDataList(
                topoDetector,
                date,
                time
        );
        if(topoData == null || topoData.isEmpty()) {
            return null;
        }

        for(int i = 0; i < vehicleGroupList.size(); i++) {
            vehicleGroupList.get(i).setVehicleGroup(vehicleGroupList.get(i).getVehicleGroup() + " (" + topoData.get(i).getVehicleCountHour() + ")");
        }

        return vehicleGroupList;
    }
}
