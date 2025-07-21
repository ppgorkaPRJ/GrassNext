package com.grassnext.grassnextserver.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

/**
 * Represents data collected during a measurement.
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MeasurementData {
    /**
     * A list containing the counts of vehicles for the specific measurement.
     *
     */
    List<Integer> vehicles;
    /**
     * Represents the weather stability class for a specific measurement.
     *
     */
    int weatherStability;
    /**
     * Represents the type of pollution measurement being recorded.
     *
     * This variable can be used to distinguish between different categories
     * or sources of pollution, such as air pollution, water pollution, etc.,
     * allowing for more specific data collection and analysis.
     */
    int pollutionType;
    /**
     * Represents the unique identifier of the detector used for data collection or measurement.
     * This value is used to associate and retrieve information specific to a particular detector.
     */
    int detectorId;
    /**
     * Represents the date associated with a specific measurement.
     *
     * This variable is used to specify the date for which the measurement data
     * was collected. The date is stored as a {@link LocalDate} object.
     */
    LocalDate date;
    /**
     * Represents the specific hour of the day associated with a given measurement.
     * This value is stored as an integer, where typically 0 indicates midnight
     * and 23 represents 11 PM. It is used to track the temporal context of data
     * within a day for analysis purposes.
     */
    int time;
    /**
     * Represents the geographical or administrative area associated with
     * the measurement data. This value is typically used to identify
     * the specific location or region where the measurement was taken.
     */
    int area;
}
