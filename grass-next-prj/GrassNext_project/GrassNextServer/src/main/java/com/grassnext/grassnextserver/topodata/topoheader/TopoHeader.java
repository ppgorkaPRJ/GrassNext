package com.grassnext.grassnextserver.topodata.topoheader;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Represents the header data for a topo file.
 *
 */
@Data
@NoArgsConstructor
public class TopoHeader {
    /**
     * Represents the code of the producer or originator of the topo data.
     *
     */
    private String producerCode;
    /**
     * Represents the unique identifier of the topo device.
     *
     */
    private String deviceId;
    /**
     * Represents the classification code from the topo file.
     *
     */
    private int classificationCode;
    /**
     * Represents the rn value from the topo file.
     *
     */
    private String rn;
    /**
     * Represents the rf value from the topo file.
     *
     */
    private String rf;
    /**
     * Represents the measurement start time.
     *
     */
    private LocalDateTime startTime;
    /**
     * Represents the measurement end time.
     *
     */
    private LocalDateTime endTime;
    /**
     * Represents the version of the hardware.
     *
     */
    private String hardwareVersion;
    /**
     * Represents the data export start time.
     *
     */
    private LocalDateTime exportStartTime;
    /**
     * Represents the data export end time.
     *
     */
    private LocalDateTime exportEndTime;
    /**
     * Represents the data export format.
     *
     */
    private String exportFormat;

    /**
     * Represents the measurement number of the topo file.
     *
     */
    private int measurementNumber;
    /**
     * Represents the measurement start time.
     *
     */
    private LocalDateTime measurementStartTime;
    /**
     * Represents the measurement end time.
     *
     */
    private LocalDateTime measurementEndTime;
    /**
     * Represents the longitude of the topo detector.
     *
     */
    private double gpsLongitude;
    /**
     * Represents the latitude of the topo detector.
     *
     */
    private double gpsLatitude;
    /**
     * Represents the date and time of the GPS location check.
     *
     */
    private LocalDateTime gpsDateTime;
    /**
     * Represents the quality value of the GPS signal.
     *
     */
    private int gpsQuality;
    /**
     * Represents the number of satellites of the GPS signal.
     *
     */
    private int gpsSatellites;
    /**
     * Represents the Horizontal Dilution of Precision (HDOP) value for the GPS.
     *
     */
    private double gpsHdop;
    /**
     * Represents the software version of the topo detector.
     *
     */
    private String softwareVersion;

    /**
     * Represents the remaining time value from the topo file.
     *
     */
    private int remainingTime;
}
