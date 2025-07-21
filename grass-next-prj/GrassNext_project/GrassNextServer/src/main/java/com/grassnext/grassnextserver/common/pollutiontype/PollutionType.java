package com.grassnext.grassnextserver.common.pollutiontype;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The PollutionType class represents a type of pollution. It is a JPA entity used for persistence in the database.
 *
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PollutionType {
    /**
     * The unique identifier for the PollutionType entity.
     * This field is the primary key of the entity and is auto-generated
     * using the GenerationType.IDENTITY strategy.
     *
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * A chemical formula of the pollution type (with footnotes and full chemical formula name).
     *
     */
    @Column(nullable = false)
    private String pollutionType;
    /**
     * Additional textual description providing a simplified record of the chemical formula of the pollution type.
     *
     */
    @Column
    private String description;

    /**
     * Constructs a new instance of the PollutionType class.
     *
     * @param id             the unique identifier for the pollution type
     * @param pollutionType  the name of the pollution type
     */
    public PollutionType(Long id, String pollutionType) {
        this.id = id;
        this.pollutionType = pollutionType;
    }
}