package com.milosz.cantor.rate;

import com.milosz.cantor.nbp.NbpCurrencyCode;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Map;
import java.util.stream.Collectors;

public record RatesFetchedEvent(
        Instant fetchedAt,
        LocalDate effectiveDate,
        Map<CurrencyCode, BigDecimal> rates) {

    public static RatesFetchedEvent of(Instant fetchedAt, LocalDate effectiveDate, Map<NbpCurrencyCode, BigDecimal> rates) {
        return new RatesFetchedEvent(fetchedAt, effectiveDate, getRates(rates));
    }

    private static Map<CurrencyCode, BigDecimal> getRates(Map<NbpCurrencyCode, BigDecimal> rates) {
        return rates.entrySet()
                .stream()
                .collect(Collectors.toMap(
                        x -> CurrencyCode.nbpCurrencyMapper().apply(x.getKey()),
                        Map.Entry::getValue
                ));
    }

}
