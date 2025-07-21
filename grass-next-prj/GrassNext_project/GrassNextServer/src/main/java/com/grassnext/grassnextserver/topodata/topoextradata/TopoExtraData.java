package com.grassnext.grassnextserver.topodata.topoextradata;

import com.grassnext.grassnextserver.util.converters.LocalDateConverter;
import com.grassnext.grassnextserver.util.converters.LocalTimeConverter;
import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvCustomBindByName;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Represents the extra data for a topo file.
 *
 */
@Data
public class TopoExtraData {
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
     * Represents a topo code parsed and converted from a CSV column named "Code".
     *
     */
    @CsvBindByName(column = "Code")
    private int code;
    /**
     * Represents a topo value parsed and converted from a CSV column named "Wert".
     *
     */
    @CsvBindByName(column = "Wert")
    private String value;

    /**
     * Sets the date and time by combining the provided {@code LocalDate} and {@code LocalTime}
     * into a {@code LocalDateTime} and assigns it to the {@code dateTime} field.
     *
     * @param date the date component to be set, represented by a {@code LocalDate}
     * @param time the time component to be set, represented by a {@code LocalTime}
     */
    public void setDateTime(LocalDate date, LocalTime time) {
        this.dateTime = LocalDateTime.of( date, time );
    }
}
