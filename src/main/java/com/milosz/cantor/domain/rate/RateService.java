package com.milosz.cantor.domain.rate;

import com.milosz.cantor.infrastructure.nbp.NbpClient;
import com.milosz.cantor.infrastructure.nbp.NbpResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Map;

@Service
public class RateService {

    private final RateSnapshotRepository snapshotRepository;
    private final NbpClient nbpClient;

    public RateService(RateSnapshotRepository snapshotRepository, NbpClient nbpClient) {
        this.snapshotRepository = snapshotRepository;
        this.nbpClient = nbpClient;
    }

    public RateSnapshot getLatestRates() {
        return snapshotRepository.findFirstByOrderByEffectiveDateDesc().orElse(null);
    }

    @Transactional
    public RateSnapshot fetchRatesFromNbp() {
        LocalDate today = LocalDate.now();

        if (snapshotRepository.existsByEffectiveDate(today)) {
            return snapshotRepository.findFirstByOrderByEffectiveDateDesc().orElse(null);
        }

        NbpResponse nbpResponse = nbpClient.fetchTodayRates();

        RateSnapshot snapshot = new RateSnapshot(
            CurrencyCode.PLN,
            "NBP",
            Instant.now(),
            nbpResponse.getEffectiveDate()
        );
    
        Map<CurrencyCode, BigDecimal> rawRates = nbpResponse.getRates();


        rawRates.forEach((currencyCode, value) -> {
            Rate rate = new Rate(currencyCode, value, snapshot);
            snapshot.addRate(rate);
        });


        return snapshotRepository.save(snapshot);
    }
}
