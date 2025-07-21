package com.grassnext.grassnextserver.topodata.topomaindata;

import com.grassnext.grassnextserver.util.converters.VehicleCodeConverter;
import com.grassnext.grassnextserver.util.enums.VehicleCodeEnum;
import com.grassnext.grassnextserver.util.enums.VehicleGroupEnum;
import com.grassnext.grassnextserver.util.converters.LocalDateConverter;
import com.grassnext.grassnextserver.util.converters.LocalTimeConverter;
import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvCustomBindByName;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Represents the main body data for a topo file.
 *
 */
@Data
@NoArgsConstructor
public class TopoMainData {
    /**
     * Represents the date and time when a specific event or measurement occurred.
     *
     */
    private LocalDateTime dateTime;
    /**
     * Represents a date parsed and converted from a CSV column named "Datum".
     *
     */
    @CsvCustomBindByName(column = "Datum", converter = LocalDateConverter.class)
    private LocalDate date;
    /**
     * Represents a time parsed and converted from a CSV column named "Uhrzeit".
     *
     */
    @CsvCustomBindByName(column = "Uhrzeit", converter = LocalTimeConverter.class)
    private LocalTime time;
    /**
     * Represents a velocity parsed and converted from a CSV column named "V".
     *
     */
    @CsvBindByName(column = "V")
    private int velocity;
    /**
     * Represents a vehicle length parsed and converted from a CSV column named "L".
     *
     */    @CsvBindByName(column = "L")
    private int vehicleLength;
    /**
     * Represents a time gap parsed and converted from a CSV column named "Zeit".
     *
     */    @CsvBindByName(column = "Zeit")
    private int timeGap;
    /**
     * Represents a driving direction parsed and converted from a CSV column named "FS".
     *
     */    @CsvBindByName(column = "FS")
    private int drivingDirection;
    /**
     * Represents a vehicle group represented as an enumerated type.
     *
     */
    @Column(nullable = false)
    private VehicleGroupEnum vehicleGroup;
    /**
     * Represents a vehicle code parsed and converted from a CSV column named "Code".
     *
     */    @CsvCustomBindByName(column = "Code", converter = VehicleCodeConverter.class)
    private VehicleCodeEnum vehicleCode;
    /**
     * Represents an audible level (in decibels) parsed and converted from a CSV column named "Schall".
     *
     */    @CsvBindByName(column = "Schall")
    private int audibleDbLevel;
    /**
     * Represents a number of axes of the vehicle parsed and converted from a CSV column named "AZ".
     *
     */    @CsvBindByName(column = "AZ")
    private int axisNumber;
    /**
     * Represents a status parsed and converted from a CSV column named "Sta".
     *
     */    @CsvBindByName(column = "Sta")
    private int status;

    /**
     * Sets the date and time by combining the provided {@code LocalDate} and {@code LocalTime}
     * into a {@code LocalDateTime} object and assigns it to the {@code dateTime} field.
     *
     * @param date the {@code LocalDate} to be used for setting the date part
     * @param time the {@code LocalTime} to be used for setting the time part
     */
    public void setDateTime(LocalDate date, LocalTime time) {
        this.dateTime = LocalDateTime.of( date, time );
    }
}
