package com.grassnext.grassnextserver.common.pollutiontype.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * PollutionTypeDto is a Data Transfer Object (DTO) representing a pollution type.
 *
 */
@Data
@NoArgsConstructor
public class PollutionTypeDto {
    /**
     * The unique identifier for the PollutionTypeDto.
     *
     */
    private Long id;
    /**
     * A chemical formula of the pollution type (with footnotes and full chemical formula name).
     *
     */
    private String pollutionType;
    /**
     * Additional textual description providing a simplified record of the chemical formula of the pollution type.
     *
     */
    private String description;
}
