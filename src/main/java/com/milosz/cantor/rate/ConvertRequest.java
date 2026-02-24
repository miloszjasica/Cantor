package com.milosz.cantor.rate;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

record ConvertRequest(
        @NotNull
        CurrencyCode fromCurrency,
        
        @NotNull
        CurrencyCode toCurrency,
        
        @NotNull
        @DecimalMin(value = "0.01", inclusive = true, message = "Amount must be greater than zero")
        BigDecimal amount
) {}
