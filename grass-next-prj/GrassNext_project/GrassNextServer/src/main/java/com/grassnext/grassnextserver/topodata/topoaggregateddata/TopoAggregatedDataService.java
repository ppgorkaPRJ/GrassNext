package com.grassnext.grassnextserver.topodata.topoaggregateddata;

import com.grassnext.grassnextserver.topodata.topodetector.TopoDetector;
import com.grassnext.grassnextserver.topodata.topodetector.TopoDetectorRepository;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

/**
 * Service class responsible for managing REST operations related to TopoAggregatedData entities.
 *
 */
@Service
@Data
@NoArgsConstructor
public class TopoAggregatedDataService {
    /**
     * Dependency-injected repository used to perform CRUD operations on TopoAggregatedData entities.
     *
     */
    TopoAggregatedDataRepository topoAggregatedDataRepository;
    /**
     * Dependency-injected repository used to perform CRUD operations on TopoDetector entities.
     *
     */
    TopoDetectorRepository topoDetectorRepository;

    /**
     * Constructs a new instance of TopoAggregatedDataService.
     *
     * @param topoAggregatedDataRepository the repository used for CRUD operations on {@link TopoAggregatedData} entities
     * @param topoDetectorRepository the repository used for CRUD operations on {@link TopoDetector} entities
     *
     */
    @Autowired
    TopoAggregatedDataService(
            TopoAggregatedDataRepository topoAggregatedDataRepository,
            TopoDetectorRepository topoDetectorRepository
    ) {
        this.topoAggregatedDataRepository = topoAggregatedDataRepository;
        this.topoDetectorRepository = topoDetectorRepository;
    }

    /**
     * Retrieves a list of unique measurement dates associated with a specified topo detector.
]     *
     * @param topoId the unique identifier of the topo detector
     * @return a list of unique measurement dates associated with the specified topo detector,
     *         or null if the detector is not found
     */
    public List<LocalDate> getTopoDates(long topoId) {
        TopoDetector topoDetector = topoDetectorRepository.findTopoDetectorById(topoId);
        if(topoDetector != null) {
            return topoAggregatedDataRepository.findTopoAggregatedDataDates(topoDetector);
        }

        return null;
    }
}
