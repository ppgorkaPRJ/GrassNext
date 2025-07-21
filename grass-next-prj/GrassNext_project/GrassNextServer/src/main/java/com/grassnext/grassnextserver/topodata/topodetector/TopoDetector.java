package com.grassnext.grassnextserver.topodata.topodetector;

import com.grassnext.grassnextserver.locationdata.LocationData;
import com.grassnext.grassnextserver.util.gpspoint.GpsPoint;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The TopoDetector class represents a topo detector. It is a JPA entity used for persistence in the database.
 *
 */
@Entity
@Data
@NoArgsConstructor
public class TopoDetector {
    /**
     * The unique identifier for the TopoDetector.
     *
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * An identifier of the topo detector device.
     *
     */
    @Column(nullable = false)
    private String deviceId;
    /**
     * A name of the location associated with the topo detector.
     *
     */
    @OneToOne
    @JoinColumn(nullable = false, referencedColumnName = "id")
    private LocationData locationData;
    /**
     * GPS coordinates of the topo detector.
     *
     */
    @Column(nullable = false)
    @Embedded
    private GpsPoint topoLocation;


    /**
     * Constructs a new instance of the TopoDetector class.
     *
     * @param deviceId      an identifier of the topo detector device
     * @param locationData  a name of the location associated with the topo detector
     * @param topoLocation  GPS coordinates of the topo detector
     */
    public TopoDetector(
            String deviceId,
            LocationData locationData,
            GpsPoint topoLocation
            ) {
        this.deviceId = deviceId;
        this.locationData = locationData;
        this.topoLocation = topoLocation;
    }
}
