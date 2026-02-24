package com.milosz.cantor.rate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.Optional;

@Repository
interface RateSnapshotRepository extends JpaRepository<RateSnapshotEntity, Long> {

    Optional<RateSnapshotEntity> findFirstByOrderByEffectiveDateDesc();

    boolean existsByEffectiveDate(LocalDate effectiveDate);

}