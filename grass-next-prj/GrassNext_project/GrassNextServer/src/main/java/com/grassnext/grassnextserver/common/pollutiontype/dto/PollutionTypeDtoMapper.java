package com.grassnext.grassnextserver.common.pollutiontype.dto;

import com.grassnext.grassnextserver.common.pollutiontype.PollutionType;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * A service responsible for mapping {@link PollutionType} entities to {@link PollutionTypeDto} objects.
 *
 */
@Service
public class PollutionTypeDtoMapper {
    /**
     * Maps a {@link PollutionType} entity to a {@link PollutionTypeDto} Data Transfer Object.
     *
     * @param pollutionType the {@link PollutionType} entity to be mapped
     * @return a {@link PollutionTypeDto} containing the mapped data from the {@link PollutionType} entity
     */
    public PollutionTypeDto map(PollutionType pollutionType) {
        PollutionTypeDto dto = new PollutionTypeDto();
        dto.setId(pollutionType.getId());
        dto.setPollutionType(pollutionType.getPollutionType());
        dto.setDescription(pollutionType.getDescription());

        return dto;
    }

    /**
     * Maps a list of {@link PollutionType} entities to a list of {@link PollutionTypeDto} objects.
     *
     * @param pollutionTypeList the list of {@link PollutionType} entities to be mapped; must not be null
     * @return a list of {@link PollutionTypeDto} objects representing the mapped entities;
     *         an empty list is returned if the input list is empty or contains only null elements
     */
    public List<PollutionTypeDto> mapAll(List<PollutionType> pollutionTypeList) {
        List<PollutionTypeDto> pollutionTypeDtoList = new ArrayList<>();
        if (!pollutionTypeList.isEmpty()) {
            for (PollutionType pollutionType : pollutionTypeList) {
                if (pollutionType != null) {
                    pollutionTypeDtoList.add( map(pollutionType) );
                }
            }
        }

        return pollutionTypeDtoList;
    }
}
