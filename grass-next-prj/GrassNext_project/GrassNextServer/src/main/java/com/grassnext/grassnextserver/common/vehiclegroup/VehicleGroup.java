package com.grassnext.grassnextserver.common.vehiclegroup;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The VehicleGroup class represents a vehicle group. It is a JPA entity used for persistence in the database.
 *
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VehicleGroup {
    /**
     * The unique identifier for the VehicleGroup entity.
     * This field is the primary key of the entity and is auto-generated
     * using the GenerationType.IDENTITY strategy.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     *  A field containing an HTML icon for a specified vehicle group.
     *
     */
    @Column(nullable = false)
    private String vehicleGroup;
    /**
     *  Textual description providing a vehicle category name.
     *
     */
    @Column
    private String description;

    /**
     * Constructs a new instance of the VehicleGroup class.
     *
     * @param id             the unique identifier for the vehicle group
     * @param vehicleGroup  the name of the vehicle group
     */
    public VehicleGroup(Long id, String vehicleGroup) {
        this.id = id;
        this.vehicleGroup = vehicleGroup;
    }
}
