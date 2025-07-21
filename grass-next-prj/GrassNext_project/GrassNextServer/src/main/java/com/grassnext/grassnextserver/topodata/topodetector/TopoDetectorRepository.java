package com.grassnext.grassnextserver.topodata.topodetector;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Repository interface for TopoDetector entities.
 *
 */
public interface TopoDetectorRepository extends JpaRepository<TopoDetector, Long> {
    /**
     * Custom method to retrieve a TopoDetector entity by its ID.
     *
     * @param id topo device identifier
     * @return TopoDetector entity for the given ID
     */
    TopoDetector findTopoDetectorById(long id);
    /**
     * Custom method to retrieve a TopoDetector entity by latitude, longitude and deviceId.
     *
     * @param longitude the longitude of the detector
     * @param latitude the latitude of the detector
     * @param deviceId the device ID of the detector
     * @return TopoDetector entity for the given latitude, longitude and deviceId
     */
    @Query("SELECT t FROM TopoDetector t WHERE t.topoLocation.longitude = ?1 AND t.topoLocation.latitude = ?2 AND t.deviceId = ?3")
    TopoDetector findExistingTopoDetector(double longitude, double latitude, String deviceId);
    /**
     * Custom method to retrieve a TopoDetector entity by its location.
     *
     * @param country the country
     * @param state the state
     * @param town the town
     * @return a list of TopoDetector entities for the given location
     */
    @Query("SELECT t FROM TopoDetector t WHERE t.locationData.country = ?1 AND t.locationData.state = ?2 AND t.locationData.town = ?3")
    List<TopoDetector> findTopoDetectorsByLocation(String country, String state, String town);
}
