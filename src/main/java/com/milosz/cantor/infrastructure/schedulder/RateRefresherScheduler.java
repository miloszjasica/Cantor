package com.milosz.cantor.infrastructure.schedulder;

import com.milosz.cantor.domain.rate.IRateRefresherService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class RateRefresherScheduler {

    private static final Logger LOGGER = LoggerFactory.getLogger(RateRefresherScheduler.class);

    private final IRateRefresherService rateRefresherService;

    @Autowired
    public RateRefresherScheduler(IRateRefresherService rateRefresherService) {
        this.rateRefresherService = rateRefresherService;
    }

    // co godzinę
    @Scheduled(cron = "0 0 * * * *")
    public void updateRates() {
        LOGGER.info("Scheduler: checking NBP rates...");
        rateRefresherService.fetchRatesFromNbp();
    }

}