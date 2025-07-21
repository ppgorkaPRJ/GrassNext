package com.grassnext.grassnextserver.util.gpspoint;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a GPS point with latitude and longitude coordinates. This class is embeddable
 * in other entities allowing for the storage of geographic location coordinates.
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class GpsPoint {
    /**
     * Represents the longitude coordinate of a GPS point.
     *
     */
    private Double longitude;
    /**
     * Represents the latitude coordinate of a GPS point.
     *
     */
    private Double latitude;
}