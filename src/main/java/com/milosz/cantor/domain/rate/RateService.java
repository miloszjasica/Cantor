package com.milosz.cantor.domain.rate;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class RateService {
    
    private final RateSnapshotRepository snapshotRepository;
    
    @Transactional
    public RateSnapshot createMockSnapshot() {
        if (snapshotRepository.existsByEffectiveDate(LocalDate.now())) {
            log.info("Snapshot for today already exists, returning existing one");
            return snapshotRepository.findFirstByOrderByEffectiveDateDesc().get();
        }
        
        Map<String, BigDecimal> mockRates = Map.of(
            "USD", new BigDecimal("3.98"),
            "EUR", new BigDecimal("4.32"),
            "GBP", new BigDecimal("5.12"),
            "CHF", new BigDecimal("4.56")
        );
        
        RateSnapshot snapshot = RateSnapshot.builder()
            .baseCurrency("PLN")
            .source("NBP")
            .fetchedAt(Instant.now())
            .effectiveDate(LocalDate.now())
            .build();
        
        List<Rate> rates = mockRates.entrySet().stream()
            .map(entry -> Rate.builder()
                .currency(entry.getKey())
                .rate(entry.getValue())
                .snapshot(snapshot)
                .build())
            .toList();
        
        snapshot.setRates(rates);
        
        RateSnapshot saved = snapshotRepository.save(snapshot);
        log.info("Created mock with {}", saved.getRates().size());
        
        return saved;
    }
    
    public RateSnapshot getLatestSnapshot() {
        return snapshotRepository.findFirstByOrderByEffectiveDateDesc()
            .orElseGet(() -> createMockSnapshot());
    }
}