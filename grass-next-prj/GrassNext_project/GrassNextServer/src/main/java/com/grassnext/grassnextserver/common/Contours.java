package com.grassnext.grassnextserver.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Represents a collection of contours along with additional metadata about the processing status.
 *
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Contours {
    /**
     * Inner class representing a singular point on the map.
     *
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GpsPoint {
        /**
         * Latitude of the point on the map.
         *
         */
        public double lat;
        /**
         * Longitude of the point on the map.
         *
         */
        public double lon;
    };

    /**
     * Inner class representing a contour on the map.
     *
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Contour {
        /**
         * Threshold for which the contour is created.
         *
         */
        public String threshold;
        /**
         * Color of the contour.
         *
         */
        public String color;
        /**
         * List of {@link GpsPoint} elements that the contour consist of.
         *
         */
        public List<GpsPoint> points;
    };

    /**
     * A boolean flag indicating whether an error occurred during the processing of the contours.
     *
     */
    public boolean error;
    /**
     * A string variable used to store descriptive messages.
     *
     */
    public String msg;
    /**
     * Represents the duration of the contour processing operation.
     *
     */
    public double duration;
    /**
     * A list of contours created for the map
     *
     */
    public List<Contour> contours;
}
