package com.grassnext.grassnextserver.locationdata;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Repository interface for LocationData entities.
 *
 */
public interface LocationDataRepository extends JpaRepository<LocationData, Long> {
    LocationData findLocationDataById(Long id);
    @Query("SELECT DISTINCT l.country FROM LocationData l")
    List<String> getCountries();
    @Query("SELECT DISTINCT l.state FROM LocationData l WHERE l.country = ?1")
    List<String> getStates(String country);
    @Query("SELECT DISTINCT l.town FROM LocationData l WHERE l.country = ?1 AND l.state = ?2")
    List<String> getTowns(String country, String state);
}
