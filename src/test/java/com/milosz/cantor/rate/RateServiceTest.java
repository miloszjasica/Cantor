package com.milosz.cantor.rate;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


public class RateServiceTest {
    
    private RateService rateService;
    private RateSnapshotRepository snapshotRepository;

    @BeforeEach
    void setUp() {
        snapshotRepository = mock(RateSnapshotRepository.class);
        rateService = new RateService(snapshotRepository);
    }

    @Test
    void shouldReturnEmptyWhenNoSnapshotExist() {
        when(snapshotRepository.findFirstByOrderByEffectiveDateDesc()).thenReturn(Optional.empty());

        Optional<LatestRatesResponse> response = rateService.getLatestRatesResponse(CurrencyCode.PLN, List.of(CurrencyCode.USD));

        assertTrue(response.isEmpty());
    }

        @Test
        void shouldReturnLatestRatesResponseWhenSnapshotExists() {
        // given
        RateSnapshotEntity snapshot = mock(RateSnapshotEntity.class);

        when(snapshotRepository.findFirstByOrderByEffectiveDateDesc())
                .thenReturn(Optional.of(snapshot));

        when(snapshot.getLatest(List.of(CurrencyCode.USD)))
                .thenReturn(List.of());

        when(snapshot.getEffectiveDate())
                .thenReturn(java.time.LocalDate.of(2024, 1, 1));

        when(snapshot.getSource())
                .thenReturn("NBP");

        // when
        Optional<LatestRatesResponse> response =
                rateService.getLatestRatesResponse(
                        CurrencyCode.PLN,
                        List.of(CurrencyCode.USD)
                );

        // then
        assertTrue(response.isPresent());
        }

    @Test
    void shouldReturnEmptyWhenNoSnapshotForConversion() {
        when(snapshotRepository.findFirstByOrderByEffectiveDateDesc()).thenReturn(Optional.empty());

        Optional<ConversionResult> result = rateService.convert(new ConvertRequest(CurrencyCode.PLN, CurrencyCode.USD, java.math.BigDecimal.ONE));

        assertTrue(result.isEmpty());
    }   

    @Test
    void shouldConvertFromPlnToUsd() {
        // given
        RateSnapshotEntity snapshot = mock(RateSnapshotEntity.class);

        when(snapshotRepository.findFirstByOrderByEffectiveDateDesc())
                .thenReturn(Optional.of(snapshot));

        when(snapshot.findRate(CurrencyCode.PLN, CurrencyCode.USD))
                .thenReturn(java.math.BigDecimal.valueOf(4));

        // when
        Optional<ConversionResult> result =
                rateService.convert(
                        new ConvertRequest(
                                CurrencyCode.PLN,
                                CurrencyCode.USD,
                                java.math.BigDecimal.valueOf(8)
                        )
                );

        // then
        assertTrue(result.isPresent());
        assertTrue(result.get().to().amount().compareTo(java.math.BigDecimal.valueOf(2)) == 0);
        
     }

     @Test
     void shouldConvertFromUsdToPln() {
        // given
        RateSnapshotEntity snapshot = mock(RateSnapshotEntity.class);
        
        when(snapshotRepository.findFirstByOrderByEffectiveDateDesc())
                .thenReturn(Optional.of(snapshot));

        when(snapshot.findRate(CurrencyCode.USD, CurrencyCode.PLN))
                .thenReturn(java.math.BigDecimal.valueOf(4));

        // when
        Optional<ConversionResult> result =
                rateService.convert(
                        new ConvertRequest(
                                CurrencyCode.USD,
                                CurrencyCode.PLN,
                                java.math.BigDecimal.valueOf(8)
                        )
                );

        // then
        assertTrue(result.isPresent());
        assertTrue(result.get().to().amount().compareTo(java.math.BigDecimal.valueOf(32)) == 0);
     }

     @Test
     void shouldConvertFromPlnToPln() {
        // given
        RateSnapshotEntity snapshot = mock(RateSnapshotEntity.class);  
        
        when(snapshotRepository.findFirstByOrderByEffectiveDateDesc())
                .thenReturn(Optional.of(snapshot));

        // when
        Optional<ConversionResult> result =
                rateService.convert(
                        new ConvertRequest(
                                CurrencyCode.PLN,
                                CurrencyCode.PLN,
                                java.math.BigDecimal.valueOf(8)
                        )
                );
        
        // then
        assertTrue(result.isPresent());
        assertTrue(result.get().to().amount().compareTo(java.math.BigDecimal.valueOf(8)) == 0);
     }

}