package com.milosz.cantor.rate;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Map;

@Component
class RatesFetchedEventListener {

    private final RateSnapshotRepository snapshotRepository;

    public RatesFetchedEventListener(RateSnapshotRepository snapshotRepository) {
        this.snapshotRepository = snapshotRepository;
    }

    @EventListener
    @Transactional
    public void handleRatesFetched(RatesFetchedEvent event) {
        RateSnapshotEntity snapshot = new RateSnapshotEntity(
                CurrencyCode.PLN,
                "NBP",
                Instant.now(),
                event.effectiveDate()
        );
        event.rates().forEach((currencyCode, value) -> {
            RateEntity rate = new RateEntity(currencyCode, value, snapshot);
            snapshot.addRate(rate);
        });

        snapshotRepository.save(snapshot);
    }
}
