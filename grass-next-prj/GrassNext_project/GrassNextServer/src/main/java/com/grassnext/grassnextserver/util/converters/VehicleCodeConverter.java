package com.grassnext.grassnextserver.util.converters;

import com.grassnext.grassnextserver.util.enums.VehicleCodeEnum;
import com.opencsv.bean.AbstractBeanField;

/**
 * VehicleCodeConverter is a utility class that converts a String representation
 * of a vehicle code into a corresponding {@code VehicleCodeEnum} value.
 * The conversion is based on a predefined mapping of specific string codes
 * to vehicle types categorized in {@code VehicleCodeEnum}. Any code that does not match
 * the predefined set is categorized as NOT_CLASSIFIED.
 *
 */
public class VehicleCodeConverter extends AbstractBeanField<String, VehicleCodeEnum> {
    /**
     * Converts a given string representation of a vehicle code into a corresponding
     * {@code VehicleCodeEnum} value based on a predefined mapping.
     *
     * @param code the string representation of the vehicle code to be converted
     * @return the corresponding {@code VehicleCodeEnum} value if the code matches a predefined mapping;
     *         otherwise, returns {@code VehicleCodeEnum.NOT_CLASSIFIED}
     */
    public VehicleCodeEnum convert(String code) {
        return switch (code) {
            case "1" -> VehicleCodeEnum.CAR_GROUP;
            case "2" -> VehicleCodeEnum.CAR_WITH_TRAILER;
            case "3" -> VehicleCodeEnum.TRUCK;
            case "4" -> VehicleCodeEnum.BIG_TRUCK;
            case "5" -> VehicleCodeEnum.BUS;
            case "6" -> VehicleCodeEnum.NK_CAR;
            case "7" -> VehicleCodeEnum.PASSENGER_CAR;
            case "8" -> VehicleCodeEnum.TRUCK_WITH_TRAILER;
            case "9" -> VehicleCodeEnum.TRACTOR_TRAILER_TRUCK;
            case "10" -> VehicleCodeEnum.MOTORCYCLE;
            case "11" -> VehicleCodeEnum.SMALL_TRUCK;
            case "32" -> VehicleCodeEnum.CAR_LIKE;
            case "33" -> VehicleCodeEnum.TRUCK_LIKE;
            case "230" -> VehicleCodeEnum.BICYCLE;
            case "250" -> VehicleCodeEnum.PARTIALLY_COVERED;
            default -> VehicleCodeEnum.NOT_CLASSIFIED;
        };
    }
}
