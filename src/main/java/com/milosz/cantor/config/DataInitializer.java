package com.milosz.cantor.config;

import com.milosz.cantor.domain.rate.RateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {
    
    private final RateService rateService;
    
    @Override
    public void run(String... args) {
        log.info("Inicjalizacja danych - pobieranie z NBP...");
        try {
            var snapshot = rateService.fetchRatesFromNbp();
            log.info("Dane zainicjalizowane: {} kursów z NBP", snapshot.getRates().size());
        } catch (Exception e) {
            log.error("Błąd podczas inicjalizacji: {}", e.getMessage());
        }
    }
}