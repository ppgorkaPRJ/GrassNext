package com.grassnext.grassnextserver.topodata.topodetector.dto;

import com.grassnext.grassnextserver.util.gpspoint.GpsPoint;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * TopoDetectorDto is a Data Transfer Object (DTO) representing a topo detector.
 *
 */
@Data
@NoArgsConstructor
public class TopoDetectorDto {
    /**
     * The unique identifier for the TopoDetectorDto.
     *
     */
    private Long id;
    /**
     * A name of the location associated with the topo detector.
     *
     */
    private String locationName;
    /**
     * GPS coordinates of the topo detector.
     *
     */
    private GpsPoint topoDetectorCoordinates;
    /**
     * GPS coordinates for the starting point of a road segment, on which the topo detector was placed.
     *
     */
    private GpsPoint roadStartCoordinates;
    /**
     * GPS coordinates for the ending point of a road segment, on which the topo detector was placed.
     *
     */
    private GpsPoint roadEndCoordinates;
}
