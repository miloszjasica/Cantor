package com.milosz.cantor.rate;

import java.math.BigDecimal;

public record Money(
        CurrencyCode currency,
        BigDecimal amount
) {}

