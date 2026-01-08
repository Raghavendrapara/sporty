package org.rsc.sporty.repository;

import org.rsc.sporty.entity.Venue;
import org.rsc.sporty.entity.VenueSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VenueRepository extends JpaRepository<Venue, Long> {
    List<Venue> findByLocation(String location);
}
