package com.milosz.cantor.rate;

import com.milosz.cantor.nbp.NbpClient;
import com.milosz.cantor.nbp.NbpResponse;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Map;

@Service
class RateRefresherService implements IRateRefresherService {

    private final RateSnapshotRepository snapshotRepository;
    private final NbpClient nbpClient;

    public RateRefresherService(RateSnapshotRepository snapshotRepository, NbpClient nbpClient) {
        this.snapshotRepository = snapshotRepository;
        this.nbpClient = nbpClient;
    }

    @Override
    public RateSnapshot fetchRatesFromNbp() {
        NbpResponse nbpResponse = nbpClient.fetchTodayRates();
        LocalDate effectiveDate = nbpResponse.getEffectiveDate();

        if (snapshotRepository.existsByEffectiveDate(effectiveDate)) {
            return snapshotRepository.findFirstByOrderByEffectiveDateDesc().orElse(null);
        }

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
