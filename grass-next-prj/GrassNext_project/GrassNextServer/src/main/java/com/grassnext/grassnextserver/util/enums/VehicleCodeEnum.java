package com.grassnext.grassnextserver.util.enums;

import lombok.NoArgsConstructor;

/**
 * VehicleCodeEnum is an enumeration that represents various types of vehicles
 * using specific code values. Each type of vehicle is associated with a unique
 * integer code that can be used for classification.
 *
 */
@NoArgsConstructor
public enum VehicleCodeEnum {
    /**
     * - CAR_GROUP: Represents vehicles classified as a general car group.
     */
    CAR_GROUP(1),
    /**
     * - CAR_WITH_TRAILER: Represents cars with attached trailers.
     */
    CAR_WITH_TRAILER(2),
    /**
     * - TRUCK: Represents standard trucks.
     */
    TRUCK(3),
    /**
     * - BIG_TRUCK: Represents large size trucks.
     */
    BIG_TRUCK(4),
    /**
     * - BUS: Represents public transportation buses.
     */
    BUS(5),
    /**
     * - NK_CAR: Represents unknown or not classified cars.
     */
    NK_CAR(6),
    /**
     * - PASSENGER_CAR: Represents private passenger cars.
     */
    PASSENGER_CAR(7),
    /**
     * - TRUCK_WITH_TRAILER: Represents trucks with attached trailers.
     */
    TRUCK_WITH_TRAILER(8),
    /**
     * - TRACTOR_TRAILER_TRUCK: Represents trucks with a tractor-trailer configuration.
     */
    TRACTOR_TRAILER_TRUCK(9),
    /**
     * - MOTORCYCLE: Represents motorcycles.
     */
    MOTORCYCLE(10),
    /**
     * - SMALL_TRUCK: Represents smaller light trucks.
     */
    SMALL_TRUCK(11),
    /**
     * - CAR_LIKE: Represents vehicles with characteristics similar to cars.
     */
    CAR_LIKE(32),
    /**
     * - TRUCK_LIKE: Represents vehicles with characteristics similar to trucks.
     */
    TRUCK_LIKE(33),
    /**
     * - NOT_CLASSIFIED: Represents vehicles that cannot be classified into any other category.
     */
    NOT_CLASSIFIED(64),
    /**
     * - BICYCLE: Represents bicycles.
     */
    BICYCLE(230),
    /**
     * - PARTIALLY_COVERED: Represents partially covered vehicles.
     */
    PARTIALLY_COVERED(250);

    /**
     * Constructs a new instance of the VehicleCodeEnum class.
     *
     */
    VehicleCodeEnum(int value) {
    }
}
