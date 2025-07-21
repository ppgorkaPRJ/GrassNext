package com.grassnext.grassnextserver.topodata.topoaggregateddata;

import com.grassnext.grassnextserver.topodata.topoaggregateddata.dto.TopoAggregatedDataDto;
import com.grassnext.grassnextserver.topodata.topoaggregateddata.dto.TopoAggregatedDataDtoMapper;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * TopoAggregatedDataController is a REST controller that provides endpoints for accessing
 * and managing aggregated topographical data.
 *
 */
@RestController
@RequestMapping("/api/data/")
@NoArgsConstructor
public class TopoAggregatedDataController {
    /**
     * Service instance for managing topo aggregated data operations.
     *
     */
    TopoAggregatedDataService topoAggregatedDataService;
    /**
     * Maps topo aggregated data entities to Data Transfer Objects (DTOs).
     *
     */
    TopoAggregatedDataDtoMapper topoAggregatedDataDtoMapper;

    /**
     * Constructs a new instance of TopoAggregatedDataController with the specified service
     * and mapper components.
     *
     * @param topoAggregatedDataService the service used for managing aggregated topographical data operations
     * @param topoAggregatedDataDtoMapper the mapper used for converting entities to Data Transfer Objects (DTOs)
     */
    @Autowired
    TopoAggregatedDataController(
            TopoAggregatedDataService topoAggregatedDataService,
            TopoAggregatedDataDtoMapper topoAggregatedDataDtoMapper
    ) {
        this.topoAggregatedDataService = topoAggregatedDataService;
        this.topoAggregatedDataDtoMapper = topoAggregatedDataDtoMapper;
    }

    /**
     * Retrieves all aggregated topographical data dates for a specific topo detector.
     *
     * @param topoId the unique identifier of the topo detector whose aggregated data dates are to be retrieved
     * @return a ResponseEntity containing a list of TopoAggregatedDataDto objects representing the dates of the aggregated data
     */
    @GetMapping("dates")
    public ResponseEntity<List<TopoAggregatedDataDto>> getAllTopoDetectors(@RequestParam long topoId) {
        return ResponseEntity.ok( topoAggregatedDataDtoMapper.mapAll( topoAggregatedDataService.getTopoDates(topoId) ) );
    }
}
