package com.milosz.cantor.domain.rate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RateSnapshotRepository extends JpaRepository<RateSnapshot, UUID> {

    Optional<RateSnapshot> findFirstByOrderByEffectiveDateDesc();

    boolean existsByEffectiveDate(LocalDate effectiveDate);

}