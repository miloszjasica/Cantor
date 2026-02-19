package com.milosz.cantor.infrastructure.schedulder;

import com.milosz.cantor.domain.rate.RateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class RateScheduler {

    private static final Logger LOGGER = LoggerFactory.getLogger(RateScheduler.class);

    private final RateService rateService;

    public RateScheduler(RateService rateService) {
        this.rateService = rateService;
    }

    // co godzinę
    @Scheduled(cron = "0 0 * * * *")
    public void updateRates() {
        LOGGER.info("Scheduler: checking NBP rates...");
        rateService.fetchRatesFromNbp();
    }
}