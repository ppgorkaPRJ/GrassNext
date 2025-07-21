package com.grassnext.grassnextserver.common.vehiclegroup.dto;

import com.grassnext.grassnextserver.common.vehiclegroup.VehicleGroup;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * A service responsible for mapping {@link VehicleGroup} entities to {@link VehicleGroupDto} objects.
 *
 */
@Service
public class VehicleGroupDtoMapper {
    /**
     * Maps a {@link VehicleGroup} entity to a {@link VehicleGroupDto} Data Transfer Object.
     *
     * @param vehicleGroup the {@link VehicleGroup} entity to be mapped
     * @return a {@link VehicleGroupDto} containing the mapped data from the {@link VehicleGroup} entity
     */
    public VehicleGroupDto map(VehicleGroup vehicleGroup) {
        VehicleGroupDto dto = new VehicleGroupDto();
        dto.setId(vehicleGroup.getId());
        dto.setVehicleGroup(vehicleGroup.getVehicleGroup());
        dto.setDescription(vehicleGroup.getDescription());

        return dto;
    }

    /**
     * Maps a list of {@link VehicleGroup} entities to a list of {@link VehicleGroupDto} objects.
     *
     * @param vehicleGroupList the list of {@link VehicleGroup} entities to be mapped
     * @return a list of {@link VehicleGroupDto} objects corresponding to the input list
     */
    public List<VehicleGroupDto> mapAll(List<VehicleGroup> vehicleGroupList) {
        List<VehicleGroupDto> vehicleGroupDtoList = new ArrayList<>();
        if (!vehicleGroupList.isEmpty()) {
            for (VehicleGroup vehicleGroup : vehicleGroupList) {
                if (vehicleGroup != null) {
                    vehicleGroupDtoList.add( map(vehicleGroup) );
                }
            }
        }

        return vehicleGroupDtoList;
    }
}
