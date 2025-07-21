package com.grassnext.grassnextserver.topodata.topoaggregateddata;

import com.grassnext.grassnextserver.topodata.topodetector.TopoDetector;
import com.grassnext.grassnextserver.util.enums.VehicleGroupEnum;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * The TopoAggregatedData class represents the aggregated data from topo detectors. It is a JPA entity used for persistence in the database.
 *
 */
@Entity
@Data
@NoArgsConstructor
public class TopoAggregatedData {
    /**
     * The unique identifier for the TopoAggregatedData entity.
     *
     */
    @Id
    @GeneratedValue
    private Long id;

    /**
     * An identifier of the topo detector device.
     *
     */
    @JoinColumn(nullable = false)
    @ManyToOne
    private TopoDetector detectorId;
    /**
     * The date of the measurement.
     */
    @Column(nullable = false)
    private LocalDate measurementDate;
    /**
     * The hour of the measurement.
     */
    @Column(nullable = false)
    private int measurementHour;
    /**
     * The vehicle group associated with the measurement.
     */
    @Enumerated(EnumType.ORDINAL)
    private VehicleGroupEnum vehicleGroup;
    @Column(nullable = false)
    /**
     * The number of vehicles detected in the specified hour.
     */
    private int vehicleCountHour;
    /**
     * The average velocity of vehicles detected in the specified hour.
     */
    @Column(nullable = false)
    private double vehicleAvgVelocityHour;

    /**
     * Constructs a new instance of the TopoAggregatedData class.
     *
     * @param detectorId              an identifier of the topo detector device
     * @param measurementDate         the date of the measurement
     * @param measurementHour         the hour of the measurement
     * @param vehicleGroup            the vehicle group associated with the measurement
     * @param vehicleCountHour        the number of vehicles detected in the specified hour
     * @param vehicleAvgVelocityHour  the average velocity of vehicles detected in the specified hour
     */
    public TopoAggregatedData (
            TopoDetector detectorId,
            LocalDate measurementDate,
            int measurementHour,
            VehicleGroupEnum vehicleGroup,
            int vehicleCountHour,
            double vehicleAvgVelocityHour
    ) {
        this.detectorId = detectorId;
        this.measurementDate = measurementDate;
        this.measurementHour = measurementHour;
        this.vehicleGroup = vehicleGroup;
        this.vehicleCountHour = vehicleCountHour;
        this.vehicleAvgVelocityHour = vehicleAvgVelocityHour;
    }
}
