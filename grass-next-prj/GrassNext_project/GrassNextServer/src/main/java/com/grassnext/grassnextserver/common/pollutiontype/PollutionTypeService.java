package com.grassnext.grassnextserver.common.pollutiontype;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class responsible for managing REST operations related to PollutionType entities.
 *
 */
@Service
@Data
@NoArgsConstructor
public class PollutionTypeService {
    /**
     * Dependency-injected repository used to perform CRUD operations on PollutionType entities.
     *
     */
    PollutionTypeRepository pollutionTypeRepository;

    /**
     * Constructs a new instance of PollutionTypeService.
     *
     * @param pollutionTypeRepository the repository used for CRUD operations on {@link PollutionType} entities
     *
     */
    @Autowired
    PollutionTypeService(PollutionTypeRepository pollutionTypeRepository) {
        this.pollutionTypeRepository = pollutionTypeRepository;
    }

    /**
     * Retrieves all pollution types from the data repository.
     *
     * @return a list of PollutionType objects representing all the pollution types in the database
     */
    public List<PollutionType> getAllPollutionTypes() {
        return pollutionTypeRepository.findAll();
    }
}
