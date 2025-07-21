package com.grassnext.grassnextserver.topodata.topodetector.dto;

import com.grassnext.grassnextserver.common.pollutiontype.PollutionType;
import com.grassnext.grassnextserver.common.pollutiontype.dto.PollutionTypeDto;
import com.grassnext.grassnextserver.topodata.topodetector.TopoDetector;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * A service responsible for mapping {@link TopoDetector} entities to {@link TopoDetectorDtoMapper} objects.
 *
 */
@Service
public class TopoDetectorDtoMapper {
    /**
     * Maps a {@link TopoDetector} entity to a {@link TopoDetectorDto} Data Transfer Object.
     *
     * @param topoDetector the {@link TopoDetector} entity to be mapped
     * @return a {@link TopoDetectorDto} containing the mapped data from the {@link TopoDetector} entity
     */
    public TopoDetectorDto map(TopoDetector topoDetector) {
        TopoDetectorDto dto = new TopoDetectorDto();
        dto.setId(topoDetector.getId());
        dto.setLocationName(topoDetector.getLocationData().getLocationName());
        dto.setTopoDetectorCoordinates(topoDetector.getTopoLocation());
        dto.setRoadStartCoordinates(topoDetector.getLocationData().getStartNode());
        dto.setRoadEndCoordinates(topoDetector.getLocationData().getEndNode());

        return dto;
    }

    /**
     * Maps a list of {@link TopoDetector} entities to a list of {@link TopoDetectorDto} objects.
     *
     * @param topoDetectorList the list of {@link TopoDetector} entities to be mapped; must not be null
     * @return a list of {@link TopoDetectorDto} objects representing the mapped entities;
     *         an empty list is returned if the input list is empty or contains only null elements
     */
    public List<TopoDetectorDto> mapAll(List<TopoDetector> topoDetectorList) {
        List<TopoDetectorDto> topoDetectorDtoList = new ArrayList<>();
        if (!topoDetectorList.isEmpty()) {
            for (TopoDetector topoDetector : topoDetectorList) {
                if (topoDetector != null) {
                    topoDetectorDtoList.add( map(topoDetector) );
                }
            }
        }

        return topoDetectorDtoList;
    }
}
