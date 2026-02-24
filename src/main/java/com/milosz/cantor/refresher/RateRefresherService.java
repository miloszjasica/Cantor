package com.milosz.cantor.refresher;

import com.milosz.cantor.nbp.NbpClient;
import com.milosz.cantor.nbp.NbpResponse;
import com.milosz.cantor.rate.RatesFetchedEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
class RateRefresherService implements IRateRefresherService {

    private final ApplicationEventPublisher eventPublisher;
    private final NbpClient nbpClient;

    public RateRefresherService(ApplicationEventPublisher eventPublisher, NbpClient nbpClient) {
        this.eventPublisher = eventPublisher;
        this.nbpClient = nbpClient;
    }

    @Override
    public void updateRates() {
        NbpResponse nbpResponse = nbpClient.fetchTodayRates();
        RatesFetchedEvent event = RatesFetchedEvent.of(
                Instant.now(),
                nbpResponse.effectiveDate(),
                nbpResponse.rates());
        eventPublisher.publishEvent(event);
    }

}
