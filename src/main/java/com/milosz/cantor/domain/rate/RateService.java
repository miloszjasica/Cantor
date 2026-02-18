package com.milosz.cantor.domain.rate;

import com.milosz.cantor.infrastructure.nbp.NbpClient;
import com.milosz.cantor.infrastructure.nbp.NbpResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RateService {
    
    private final RateSnapshotRepository snapshotRepository;
    private final NbpClient nbpClient;
    
    @Transactional
    public RateSnapshot fetchRatesFromNbp() {
        if (snapshotRepository.existsByEffectiveDate(LocalDate.now())) {
            log.info("Today's rates already exist in the database, fetching from there...");
            return snapshotRepository.findFirstByOrderByEffectiveDateDesc().get();
        }

        NbpResponse nbpResponse = nbpClient.fetchTodayRates();

        RateSnapshot snapshot = new RateSnapshot(
            CurrencyCode.PLN,
            "NBP",
            Instant.now(),
            LocalDate.now()
        );
        
        nbpResponse.getRates().forEach((currency, value) -> {

            Rate rate = new Rate(
                CurrencyCode.valueOf(currency),
                value,
                snapshot
            );

            snapshot.addRate(rate);
        });

        
        RateSnapshot saved = snapshotRepository.save(snapshot);
        log.info("Saved snapshot with {} rates", saved.getRates().size());
        
        return saved;
    }
    
    public RateSnapshot getLatestSnapshot() {
        return snapshotRepository.findFirstByOrderByEffectiveDateDesc()
            .orElseGet(() -> fetchRatesFromNbp());
    }
}