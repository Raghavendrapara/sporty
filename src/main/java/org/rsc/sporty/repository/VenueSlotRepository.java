package org.rsc.sporty.repository;

import org.rsc.sporty.entity.VenueSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.Lock;
import java.util.Optional;

@Repository
public interface VenueSlotRepository extends JpaRepository<VenueSlot, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT vs FROM VenueSlot vs WHERE vs.id = :id")
    Optional<VenueSlot> findByIdWithLock(@Param("id") Long id);

    @Query("SELECT vs FROM VenueSlot vs WHERE vs.venue.id = :venueId " +
            "AND ((vs.startTime < :endTime AND vs.endTime > :startTime))")
    List<VenueSlot> findOverlappingSlots(@Param("venueId") Long venueId, 
                                         @Param("startTime") LocalDateTime startTime, 
                                         @Param("endTime") LocalDateTime endTime);

    @Query("SELECT vs FROM VenueSlot vs JOIN FETCH vs.venue " +
            "WHERE vs.isBooked = false " +
            "AND vs.startTime >= :startTime " +
            "AND vs.endTime <= :endTime")
    List<VenueSlot> findAvailableSlots(@Param("startTime") LocalDateTime startTime, 
                                       @Param("endTime") LocalDateTime endTime);

    List<VenueSlot> findByVenueId(Long venueId);
}
