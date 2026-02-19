package com.milosz.cantor.web.api.dto;

import java.math.BigDecimal;

import com.milosz.cantor.domain.rate.CurrencyCode;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

public record ConvertRequest(
        @NotNull
        CurrencyCode fromCurrency,
        
        @NotNull
        CurrencyCode toCurrency,
        
        @NotNull
        @DecimalMin(value = "0.01", inclusive = true, message = "Amount must be greater than zero")
        BigDecimal amount
) {}
