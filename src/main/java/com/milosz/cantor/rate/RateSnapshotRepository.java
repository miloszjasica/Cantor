package com.milosz.cantor.rate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface RateSnapshotRepository extends JpaRepository<RateSnapshot, Long> {

    Optional<RateSnapshot> findFirstByOrderByEffectiveDateDesc();

    boolean existsByEffectiveDate(LocalDate effectiveDate);

}