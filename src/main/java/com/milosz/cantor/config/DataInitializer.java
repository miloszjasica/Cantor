package com.milosz.cantor.config;

import com.milosz.cantor.domain.rate.RateService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataInitializer.class);

    private final RateService rateService;

    public DataInitializer(RateService rateService) {
        this.rateService = rateService;
    }
    
    @Override
    public void run(String... args) {
        LOGGER.info("Inicjalizacja danych - pobieranie z NBP...");
        try {
            var snapshot = rateService.fetchRatesFromNbp();
            LOGGER.info("Dane zainicjalizowane: {} kursów z NBP", snapshot.getRates().size());
        } catch (Exception e) {
            LOGGER.error("Błąd podczas inicjalizacji: {}", e.getMessage());
        }
    }
}