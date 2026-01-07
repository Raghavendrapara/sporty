package org.rsc.sporty.repository;
import org.rsc.sporty.entity.Sport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import java.util.Set;

@Repository
public interface SportRepository extends JpaRepository<Sport, Integer> {
    boolean existsByExternalId(String externalId);
    Optional<Sport> findByExternalId(String externalId);

    @Query("SELECT s.externalId FROM Sport s")
    Set<String> findAllExternalIds();
}