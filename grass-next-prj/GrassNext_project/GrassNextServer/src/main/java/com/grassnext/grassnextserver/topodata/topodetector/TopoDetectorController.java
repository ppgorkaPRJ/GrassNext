package com.grassnext.grassnextserver.topodata.topodetector;

import com.grassnext.grassnextserver.topodata.topodetector.dto.TopoDetectorDto;
import com.grassnext.grassnextserver.topodata.topodetector.dto.TopoDetectorDtoMapper;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * The TopoDetectorController serves as a REST controller for handling API requests
 * related to topo detectors.
 *
 */
@RestController
@RequestMapping("/api/detectors/")
@NoArgsConstructor
public class TopoDetectorController {
    /**
     * Service instance for managing topo detectors.
     *
     */
    TopoDetectorService topoDetectorService;
//    TopoDetectorDtoMapper_old topoDetectorDtoMapperOld;
    /**
     * Maps topo detector entities to Data Transfer Objects (DTOs).
     *
     */
    TopoDetectorDtoMapper topoDetectorDtoMapper;

    /**
     * Constructs a new instance of the TopoDetectorController.
     *
     * @param topoDetectorService the service for managing topo detectors
     * @param topoDetectorDtoMapper the mapper used to transform topo detector entities to DTOs
     */
    @Autowired
    TopoDetectorController(
            TopoDetectorService topoDetectorService,
            TopoDetectorDtoMapper topoDetectorDtoMapper
    ) {
        this.topoDetectorService = topoDetectorService;
        this.topoDetectorDtoMapper = topoDetectorDtoMapper;
    }

    /**
     * Retrieves a list of topo detectors located in a specified country, state, and town.
     *
     * @param country the name of the country where the topo detectors are located
     * @param state the name of the state within the specified country
     * @param town the name of the town within the specified state
     * @return a {@link ResponseEntity} containing a list of {@link TopoDetectorDto},
     *         which represent the topo detectors matching the specified location
     */
    @GetMapping("")
    public ResponseEntity<List<TopoDetectorDto>> getTopoDetectorsByLocation(@RequestParam String country, @RequestParam String state, @RequestParam("city") String town) {
        return ResponseEntity.ok( topoDetectorDtoMapper.mapAll( topoDetectorService.getTopoDetectorsByLocation(country, state, town) ) );
    }
}
