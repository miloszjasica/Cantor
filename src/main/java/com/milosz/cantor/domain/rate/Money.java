package com.milosz.cantor.domain.rate;

import java.math.BigDecimal;

public record Money(
        CurrencyCode currency,
        BigDecimal amount
) {}

