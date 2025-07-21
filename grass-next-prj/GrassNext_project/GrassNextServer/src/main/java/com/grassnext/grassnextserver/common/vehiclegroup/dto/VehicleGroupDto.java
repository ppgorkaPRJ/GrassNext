package com.grassnext.grassnextserver.common.vehiclegroup.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * VehicleGroupDto is a Data Transfer Object (DTO) representing a vehicle group.
 *
 */
@Data
@NoArgsConstructor
public class VehicleGroupDto {
    /**
     * The unique identifier for the PollutionTypeDto.
     *
     */
    private Long id;
    /**
     *  A field containing an HTML icon for a specified vehicle group.
     *
     */
    private String vehicleGroup;
    /**
     *  Textual description providing a vehicle category name.
     *
     */
    private String description;
}
