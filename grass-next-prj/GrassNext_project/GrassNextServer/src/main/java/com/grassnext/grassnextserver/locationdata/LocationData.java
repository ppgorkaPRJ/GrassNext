package com.grassnext.grassnextserver.locationdata;

import com.grassnext.grassnextserver.util.gpspoint.GpsPoint;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Represents location data with geographical and contextual attributes
 * related to a specified area.
 *
 */
@Entity
@Data
@NoArgsConstructor
public class LocationData {
    /**
     * The unique identifier for the LocationData entity.
     * This field is the primary key of the entity and is auto-generated
     * using the GenerationType.IDENTITY strategy.
     *
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Represents the longitude coordinate of a geographical location.
     *
     */
    @Transient
    private double longitude;
    /**
     * Represents the latitude coordinate of a geographical location.
     *
     */
    @Transient
    private double latitude;
    /**
     * Represents the identifier of the topo device associated with the location data.
     *
     */
    @Transient
    private String deviceId;

    /**
     * Represents the country associated with the geographical location.
     *
     */
    @Column(nullable = false)
    private String country;
    /**
     * Represents the state of a geographical location.
     *
     */
    @Column
    private String state;
    /**
     * Represents the town of the location's address.
     *
     */
    @Column
    private String town;
    /**
     * Represents the identifier of a road within a specific location.
     *
     */
    @Column
    private String road;

    /**
     * Represents the name of the specific location.
     *
     */
    @Column(nullable = false)
    private String locationName;
    /**
     * Represents the starting GPS point of a road within the location data.
     *
     */
    @Column(nullable = false)
    @Embedded
    @AttributeOverrides({
            @AttributeOverride( name = "longitude", column = @Column(nullable = false, name = "road_end_longitude")),
            @AttributeOverride( name = "latitude", column = @Column(nullable = false, name = "road_end_latitude")),
    })
    private GpsPoint startNode;
    /**
     * Represents the ending geographical GPS point of a road or path segment.
     *
     */
    @Column(nullable = false)
    @Embedded
    @AttributeOverrides({
            @AttributeOverride( name = "longitude", column = @Column(nullable = false, name = "road_start_longitude")),
            @AttributeOverride( name = "latitude", column = @Column(nullable = false, name = "road_start_latitude")),
    })
    private GpsPoint endNode;
    /**
     * Stores a list of road nodes, represented as a list of Long values.
     *
     */
    @Transient
    private List<Long> nodeIds;
}
