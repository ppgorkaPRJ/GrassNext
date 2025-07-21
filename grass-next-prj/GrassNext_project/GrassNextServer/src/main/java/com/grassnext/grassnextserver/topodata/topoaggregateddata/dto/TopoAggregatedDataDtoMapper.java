package com.grassnext.grassnextserver.topodata.topoaggregateddata.dto;

import com.grassnext.grassnextserver.common.pollutiontype.PollutionType;
import com.grassnext.grassnextserver.common.pollutiontype.dto.PollutionTypeDto;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * A service responsible for mapping {@link PollutionType} entities to {@link PollutionTypeDto} objects.
 *
 */
@Service
public class TopoAggregatedDataDtoMapper {
    /**
     * Maps a {@link LocalDate} entity to a {@link TopoAggregatedDataDto} Data Transfer Object.
     *
     * @param topoAggregatedData the {@link LocalDate} entity to be mapped
     * @return a {@link TopoAggregatedDataDto} containing the mapped data from the {@link LocalDate} entity
     */
    public TopoAggregatedDataDto map(LocalDate topoAggregatedData) {
        TopoAggregatedDataDto dto = new TopoAggregatedDataDto();

        dto.setDay(topoAggregatedData.getDayOfMonth());
        dto.setMonth(topoAggregatedData.getMonthValue());
        dto.setYear(topoAggregatedData.getYear());

        return dto;
    }

    /**
     * Maps a list of {@link LocalDate} entities to a list of {@link TopoAggregatedDataDto} objects.
     *
     * @param topoAggregatedDataList the list of {@link LocalDate} entities to be mapped; must not be null
     * @return a list of {@link TopoAggregatedDataDto} objects representing the mapped entities;
     *         an empty list is returned if the input list is empty or contains only null elements
     */
    public List<TopoAggregatedDataDto> mapAll(List<LocalDate> topoAggregatedDataList) {
        List<TopoAggregatedDataDto> topoAggregatedDataDtoList = new ArrayList<>();
        if (!topoAggregatedDataList.isEmpty()) {
            for (LocalDate topoAggregatedData : topoAggregatedDataList) {
                if (topoAggregatedData != null) {
                    topoAggregatedDataDtoList.add( map(topoAggregatedData) );
                }
            }
        }

        return topoAggregatedDataDtoList;
    }
}
