package com.grassnext.grassnextserver.topodata.topoaggregateddata;

import com.grassnext.grassnextserver.topodata.topodetector.TopoDetector;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

/**
 * Repository interface for TopoAggregatedData entities.
 *
 */
public interface TopoAggregatedDataRepository extends JpaRepository<TopoAggregatedData, Long> {
    /**
     * Retrieves a list of TopoAggregatedData entities based on the specified detector, measurement date, and measurement hour.
     * The result is filtered to exclude records where the vehicle group equals 0 and is ordered by the vehicle group.
     *
     * @param detectorId       the identifier of the topo detector device
     * @param measurementDate  the date of the measurement
     * @param measurementHour  the hour of the measurement
     * @return a list of TopoAggregatedData entities matching the specified criteria
     */
    @Query("SELECT t FROM TopoAggregatedData t WHERE t.detectorId = ?1 AND t.measurementDate = ?2 AND t.measurementHour = ?3 AND t.vehicleGroup != 0 ORDER BY t.vehicleGroup")
    List<TopoAggregatedData> findTopoAggregatedDataList(TopoDetector detectorId, LocalDate measurementDate, int measurementHour);

    /**
     * Retrieves a distinct list of measurement dates for the specified topo detector.
     *
     * @param detectorId the identifier of the topo detector for which the measurement dates are to be retrieved
     * @return a list of unique measurement dates associated with the given topo detector
     */
    @Query("SELECT DISTINCT t.measurementDate FROM TopoAggregatedData t WHERE t.detectorId = ?1")
    List<LocalDate> findTopoAggregatedDataDates(TopoDetector detectorId);
}
