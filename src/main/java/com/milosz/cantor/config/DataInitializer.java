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
        log.info("Data Initializing...");
        var snapshot = rateService.createMockSnapshot();
        log.info("Initialized data: {}", snapshot.getRates().size());
    }
}