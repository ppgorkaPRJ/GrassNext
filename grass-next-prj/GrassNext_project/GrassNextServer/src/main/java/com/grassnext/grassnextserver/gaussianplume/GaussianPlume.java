package com.grassnext.grassnextserver.gaussianplume;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * The GaussianPlume class represents the Gaussian Plume data necessary for simulation.
 *
 */
@Data
@Builder(builderMethodName = "GaussianPlumeBuilder")
public class GaussianPlume {
    /**
     * Inner class representing singular point on the map.
     *
     */
    @Data
    @AllArgsConstructor
    public static class GpsPoint {
        /**
         * Represents the longitude component of a geographical coordinate.
         *
         */
        double lon;
        /**
         * Represents the latitude of a geographical point.
         *
         */
        double lat;
    }
    /**
     * Inner class representing data related to wind conditions.
     *
     */
    @Data
    @AllArgsConstructor
    public static class Wind {
        /**
         * Represents the wind speed measured in meters per second (m/s).
         *
         */
        double speed;
        /**
         * Represents the direction of the wind in degrees.
         *
         */
        int direction;
        /**
         * Represents the stability class of atmospheric conditions, which ranges
         * from 1 to 6. This value is used to categorize weather stability as:
         * 1 - Extremely unstable,
         * 2 - Moderately unstable,
         * 3 - Slightly unstable,
         * 4 - Neutral,
         * 5 - Slightly stable,
         * 6 - Moderately stable.
         */
        int stability; //[1-6]
    }
    /**
     * Inner class representing vehicle counts and average velocities.
     *
     */
    @Data
    @AllArgsConstructor
    public static class Vehicle {
        /**
         * Indicates whether the vehicle instance has been selected.
         *
         */
        boolean chosen;
        /**
         * Represents the count of vehicles of a specific type.
         *
         */
        int count;
        /**
         * Represents the average velocity of vehicles of a specific type.
         */
        double avgVelocity;
    }

    /**
     * List of {@link Vehicle} object instances for each vehicle type.
     *
     */
    public List<Vehicle> vehicles;
    /**
     * {@link Wind} object instance describing the wind conditions.
     *
     */
    public Wind wind;
    /**
     * {@link GpsPoint} object representing the road starting point.
     *
     */
    public GpsPoint roadStart;
    /**
     *
     * {@link GpsPoint} object representing the road ending point.
     *
     */
    public GpsPoint roadEnd;
    /**
     * Specifies the pollution concentration threshold levels used in the simulation.
     *
     */
    public int thresholds;

    /**
     * Represents the type of air pollution in the Gaussian Plume model.
     *
     */
    public int pollutionType; //[1-3]

    /**
     * Represents the physical height of the emission source (e.g., exhaust pipes) above the ground.
     *
     */
    public double emittersHeight;   //0.3m
    /**
     * Represents the height above ground level at which pollutant concentration
     * is calculated.
     *
     */
    public double concentrationHeight; //2.0m

    /**
     * Specifies the size of the computational matrix (in meters) used in the Gaussian Plume model simulations.
     *
     */
    public int matrixSize; //4000
    /**
     * Represents the spatial resolution of each cell in the simulation area matrix.
     *
     */
    public double cellResolution; //1,0m

    /**
     * Represents the division factor for the matrix's side length
     * in the Gaussian Plume model simulation.
     *
     */
    public int divMatrixSide; //8
}