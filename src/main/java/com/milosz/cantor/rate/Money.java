package com.milosz.cantor.rate;

import java.math.BigDecimal;

record Money(CurrencyCode currency, BigDecimal amount) {}

