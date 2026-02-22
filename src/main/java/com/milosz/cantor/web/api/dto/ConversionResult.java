package com.milosz.cantor.web.api.dto;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;

import com.milosz.cantor.domain.rate.CurrencyCode;
import com.milosz.cantor.domain.rate.Money;
import com.milosz.cantor.domain.rate.RateSnapshot;
import com.milosz.cantor.domain.rate.Rounding;

public record ConversionResult(
        Money from,
        Money to,
        BigDecimal rate,
        Instant asOf,
        String source,
        Rounding rounding
) {

    public static ConversionResult of(
            CurrencyCode from,
            CurrencyCode to,
            BigDecimal fromAmount,
            BigDecimal toAmount,
            BigDecimal rate,
            RateSnapshot snapshot
    ) {
        return new ConversionResult(
                new Money(from, fromAmount.setScale(2)),
                new Money(to, toAmount),
                rate,
                snapshot.getFetchedAt(),
                snapshot.getSource(),
                new Rounding(2, RoundingMode.HALF_UP)
        );
    }

    public static ConversionResult sameCurrency(
            CurrencyCode currency,
            BigDecimal amount,
            RateSnapshot snapshot
    ) {
        return new ConversionResult(
                new Money(currency, amount.setScale(2)),
                new Money(currency, amount.setScale(2)),
                BigDecimal.ONE,
                snapshot.getFetchedAt(),
                snapshot.getSource(),
                new Rounding(2, RoundingMode.HALF_UP)
        );
    }

}

