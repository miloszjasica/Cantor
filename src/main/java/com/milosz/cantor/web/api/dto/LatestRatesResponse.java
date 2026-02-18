package com.milosz.cantor.web.api.dto;

import java.time.Instant;
import java.util.List;


public class LatestRatesResponse {
    private String base;
    private Instant asOf;
    private String source;
    private List<RateDto> rates;

    public LatestRatesResponse(String base, Instant asOf, String source, List<RateDto> rates) {
        this.base = base;
        this.asOf = asOf;
        this.source = source;
        this.rates = rates;
    }

    public String getBase() {
        return base;
    }

    public Instant getAsOf() {
        return asOf;
    }

    public String getSource() {
        return source;
    }

    public List<RateDto> getRates() {
        return rates;
    }

    public static class RateDto {
        private String symbol;
        private String rate;

        public RateDto(String symbol, String rate) {
            this.symbol = symbol;
            this.rate = rate;
        }

        public String getSymbol() {
            return symbol;
        }

        public String getRate() {
            return rate;
        }
    }
}