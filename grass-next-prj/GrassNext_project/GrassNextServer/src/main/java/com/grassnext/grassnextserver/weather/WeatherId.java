package com.grassnext.grassnextserver.weather;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * The WeatherId class serves as a composite primary key for the Weather entity.
 * It identifies weather records in the database by combining measurement date, measurement hour, longitude, and latitude.
 * This class implements Serializable to ensure its compatibility with JPA and
 * enables proper serialization and deserialization processes when handling database operations.
 */
public class WeatherId implements Serializable {
    /**
     * Represents the date on which a weather measurement was taken.
     *
     */
    private LocalDate measurementDate;
    /**
     * Represents the hour of the day on which a weather measurement was taken,
     *
     */
    private int measurementHour;
    /**
     * Represents the longitude of a geographical location at which a weather measurement was taken,
     *
     */
    private double longitude;
    /**
     * Represents the latitude of a geographical location at which a weather measurement was taken,
     *
     */
    private double latitude;

    /**
     * Computes the hash code for this WeatherId object.
     * The computation is based on the values of the measurementDate,
     * measurementHour, longitude, and latitude properties.
     *
     * @return the hash code value for this WeatherId object
     */
    @Override
    public int hashCode() {
        return Objects.hash(measurementDate, measurementHour, longitude, latitude);
    }

    /**
     * Implementation of the custom equality comparison method for the WeatherId class.
     *
     * @param obj the reference object with which to compare
     * @return {@code true} if this object is the same as the {@code obj} argument; {@code false} otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        WeatherId objId = (WeatherId) obj;
        return Objects.equals(measurementDate, objId.measurementDate) &&
                Objects.equals(measurementHour, objId.measurementHour) &&
                Objects.equals(longitude, objId.longitude) &&
                Objects.equals(latitude, objId.latitude);
    }
}