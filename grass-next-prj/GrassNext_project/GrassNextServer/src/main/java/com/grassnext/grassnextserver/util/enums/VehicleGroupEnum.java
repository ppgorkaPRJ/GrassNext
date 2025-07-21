package com.grassnext.grassnextserver.util.enums;

/**
 * VehicleGroupEnum is an enumeration that categorizes vehicles into different groups
 * based on their type or classification. Each group is associated with a unique
 * integer code that can be used for classification.
 *
 */
public enum VehicleGroupEnum {
    /**
     * - NOT_CLASSIFIED: Represents a group of vehicles that cannot be classified into any other category.
     */
    NOT_CLASSIFIED(0),
    /**
     * - MOTORCYCLE_GROUP: Represents a group of vehicles classified as motorcycles.
     */
    MOTORCYCLE_GROUP(1),
    /**
     * - PASSENGER_CAR_GROUP: Represents a group of vehicles classified as passenger cars.
     */
    PASSENGER_CAR_GROUP(2),
    /**
     * - LIGHT_COMMERCIAL_VEHICLE_GROUP: Represents a group of vehicles classified as light commercial vehicles.
     */
    LIGHT_COMMERCIAL_VEHICLE_GROUP(3),
    /**
     * - HEAVY_GOODS_VEHICLE_GROUP: Represents a group of vehicles classified as heavy goods vehicles.
     */
    HEAVY_GOODS_VEHICLE_GROUP(4),
    /**
     * - PUBLIC_TRANSPORT_BUS_GROUP: Represents a group of vehicles classified as public transportation buses.
     */
    PUBLIC_TRANSPORT_BUS_GROUP(5);

    /**
     * Represents the unique integer code associated with a specific vehicle group
     * within the VehicleGroupEnum enumeration.
     *
     */
    private final int value;

    /**
     * Constructs a VehicleGroupEnum with the specified integer value.
     *
     * @param value the unique integer code associated with the specific vehicle group
     */
    VehicleGroupEnum(int value) {
        this.value = value;
    }

    /**
     * Retrieves the unique integer code associated with this vehicle group.
     *
     * @return the integer value representing the specific vehicle group
     */
    public int get() {
        return value;
    }

    /**
     * Retrieves the corresponding {@code VehicleGroupEnum} instance based on the provided integer value.
     * If the specified value does not match any predefined enumeration value, it returns {@code VehicleGroupEnum.NOT_CLASSIFIED}.
     *
     * @param value the integer value representing a specific vehicle group
     * @return the corresponding {@code VehicleGroupEnum} instance, or {@code VehicleGroupEnum.NOT_CLASSIFIED} if no match is found
     */
    public static VehicleGroupEnum getByValue(int value) {
         return switch ( value ) {
            case 1 -> VehicleGroupEnum.MOTORCYCLE_GROUP;
            case 2 -> VehicleGroupEnum.PASSENGER_CAR_GROUP;
            case 3 -> VehicleGroupEnum.LIGHT_COMMERCIAL_VEHICLE_GROUP;
            case 4 -> VehicleGroupEnum.HEAVY_GOODS_VEHICLE_GROUP;
            case 5 -> VehicleGroupEnum.PUBLIC_TRANSPORT_BUS_GROUP;
            default -> VehicleGroupEnum.NOT_CLASSIFIED;
        };
    }
}
