package com.milosz.cantor.rate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@SpringBootTest
class RateServiceIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:15-alpine")
                    .withDatabaseName("testdb")
                    .withUsername("test")
                    .withPassword("test");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.datasource.driver-class-name", postgres::getDriverClassName);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
    }

    @Autowired
    private RateService rateService;

    @Autowired
    private RateSnapshotRepository snapshotRepository;

    @Test
    void shouldConvertPlnToUsdAndUsdToPln() {
        // given
        RateSnapshotEntity snapshot = new RateSnapshotEntity(
                CurrencyCode.PLN,
                "NBP",
                Instant.now(),
                LocalDate.of(2024, 1, 1)
        );

        RateEntity usdRate = new RateEntity(CurrencyCode.USD, BigDecimal.valueOf(4), snapshot);
        snapshot.addRate(usdRate);

        snapshotRepository.save(snapshot);

        // when – PLN -> USD
        Optional<ConversionResult> plnToUsd = rateService.convert(
                new ConvertRequest(CurrencyCode.PLN, CurrencyCode.USD, BigDecimal.valueOf(8))
        );

        // then
        assertTrue(plnToUsd.isPresent());
        assertEquals(BigDecimal.valueOf(2).setScale(2), plnToUsd.get().to().amount());

        // when – USD -> PLN
        Optional<ConversionResult> usdToPln = rateService.convert(
                new ConvertRequest(CurrencyCode.USD, CurrencyCode.PLN, BigDecimal.valueOf(8))
        );

        // then
        assertTrue(usdToPln.isPresent());
        assertEquals(BigDecimal.valueOf(32).setScale(2), usdToPln.get().to().amount());
    }

    @Test
    void shouldHandleSameCurrencyConversion() {
        // given
        RateSnapshotEntity snapshot = new RateSnapshotEntity(
                CurrencyCode.PLN,
                "NBP",
                Instant.now(),
                LocalDate.of(2024, 1, 1)
        );

        snapshotRepository.save(snapshot);

        // when – PLN -> PLN
        Optional<ConversionResult> result = rateService.convert(
                new ConvertRequest(CurrencyCode.PLN, CurrencyCode.PLN, BigDecimal.valueOf(10))
        );

        // then
        assertTrue(result.isPresent());
        assertEquals(BigDecimal.valueOf(10).setScale(2), result.get().to().amount());
    }

    @Test
    void shouldThrowWhenConvertingNonPlnCurrencies() {
        // given
        RateSnapshotEntity snapshot = new RateSnapshotEntity(
                CurrencyCode.PLN,
                "NBP",
                Instant.now(),
                LocalDate.of(2024, 1, 1)
        );

        RateEntity usdRate = new RateEntity(CurrencyCode.USD, BigDecimal.valueOf(4), snapshot);
        RateEntity eurRate = new RateEntity(CurrencyCode.EUR, BigDecimal.valueOf(1.2), snapshot);

        snapshot.addRate(usdRate);
        snapshot.addRate(eurRate);

        snapshotRepository.save(snapshot);

        // when, then
        assertThrows(IllegalArgumentException.class,
                () -> rateService.convert(
                        new ConvertRequest(CurrencyCode.USD, CurrencyCode.EUR, BigDecimal.TEN)
                ));
    }
}