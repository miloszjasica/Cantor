package com.milosz.cantor.web.api.dto;

import java.math.BigDecimal;

import com.milosz.cantor.domain.rate.CurrencyCode;

public record ConvertRequest(
        CurrencyCode fromCurrency,
        CurrencyCode toCurrency,
        BigDecimal amount
) {}
