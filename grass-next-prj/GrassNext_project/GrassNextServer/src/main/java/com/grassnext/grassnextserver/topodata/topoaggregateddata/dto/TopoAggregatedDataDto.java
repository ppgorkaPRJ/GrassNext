package com.grassnext.grassnextserver.topodata.topoaggregateddata.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * TopoAggregatedDataDto is a Data Transfer Object (DTO) representing a pollution type.
 *
 */
@Data
@NoArgsConstructor
public class TopoAggregatedDataDto {
    /**
     * Represents the day of the month as part of a date.
     *
     */
    int day;
    /**
     * Represents the month as a part of a date.
     *
     */
    int month;
    /**
     * Represents the year as a part of a date.
     *
     */
    int year;
}
