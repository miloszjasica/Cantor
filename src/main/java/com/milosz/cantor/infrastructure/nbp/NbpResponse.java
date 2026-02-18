package com.milosz.cantor.infrastructure.nbp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

import com.milosz.cantor.domain.rate.CurrencyCode;

public class NbpResponse {
    private LocalDate effectiveDate;
    private Map<CurrencyCode, BigDecimal> rates;


    public NbpResponse(LocalDate effectiveDate, Map<CurrencyCode, BigDecimal> rates) {
        this.effectiveDate = effectiveDate;
        this.rates = rates;
    }
    
    public LocalDate getEffectiveDate() {
        return effectiveDate;
    }

    public Map<CurrencyCode, BigDecimal> getRates() {
        return rates;
    }
}