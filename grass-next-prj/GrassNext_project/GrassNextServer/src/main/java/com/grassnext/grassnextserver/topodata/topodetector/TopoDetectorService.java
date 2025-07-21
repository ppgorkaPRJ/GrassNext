package com.grassnext.grassnextserver.topodata.topodetector;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class responsible for managing REST operations related to TopoDetector entities.
 *
 */
@Service
@Data
@NoArgsConstructor
public class TopoDetectorService {
    /**
     * Dependency-injected repository used to perform CRUD operations on TopoDetector entities.
     *
     */
    TopoDetectorRepository topoDetectorRepository;

    /**
     * Constructs a new instance of TopoDetectorService.
     *
     * @param topoDetectorRepository the repository used for CRUD operations on {@link TopoDetector} entities
     *
     */
    @Autowired
    TopoDetectorService(TopoDetectorRepository topoDetectorRepository) {
        this.topoDetectorRepository = topoDetectorRepository;
    }

    /**
     * Retrieves all TopoDetector entities from the repository.
     *
     * @return a list of TopoDetector entities representing all topo detectors present in the database
     */
    public List<TopoDetector> getAllTopoDetectors() {
        return topoDetectorRepository.findAll();
    }
    /**
     * Retrieves a list of TopoDetector entities based on the specified location parameters.
     *
     * @param country the country where the topo detectors are located
     * @param state the state where the topo detectors are located
     * @param town the town where the topo detectors are located
     * @return a list of TopoDetector entities matching the specified location
     */
    public List<TopoDetector> getTopoDetectorsByLocation(String country, String state, String town) {
        return topoDetectorRepository.findTopoDetectorsByLocation(country, state, town);
    }
}