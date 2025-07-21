package com.grassnext.grassnextserver.common.pollutiontype;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for PollutionType entities.
 *
 */
public interface PollutionTypeRepository extends JpaRepository<PollutionType, Long>  {
}