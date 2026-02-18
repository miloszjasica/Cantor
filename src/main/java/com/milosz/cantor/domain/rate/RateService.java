package com.milosz.cantor.domain.rate;

import com.milosz.cantor.infrastructure.nbp.NbpClient;
import com.milosz.cantor.infrastructure.nbp.NbpResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.Instant;
import java.time.LocalDate;

@Service
public class RateService {
    private static final Logger LOGGER = LoggerFactory.getLogger(RateService.class);

    private final RateSnapshotRepository snapshotRepository;
    private final NbpClient nbpClient;

    public RateService(RateSnapshotRepository snapshotRepository, NbpClient nbpClient) {
        this.snapshotRepository = snapshotRepository;
        this.nbpClient = nbpClient;
    }
    
    @Transactional
    public RateSnapshot fetchRatesFromNbp() {
        if (snapshotRepository.existsByEffectiveDate(LocalDate.now())) {
            LOGGER.info("Today's rates already exist in the database, fetching from there...");
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
                currency,
                value,
                snapshot
            );
            snapshot.addRate(rate);
        });


        
        RateSnapshot saved = snapshotRepository.save(snapshot);
        LOGGER.info("Saved snapshot with {} rates", saved.getRates().size());
        
        return saved;
    }
    
    public RateSnapshot getLatestSnapshot() {
        return snapshotRepository.findFirstByOrderByEffectiveDateDesc()
            .orElseGet(() -> fetchRatesFromNbp());
    }
}