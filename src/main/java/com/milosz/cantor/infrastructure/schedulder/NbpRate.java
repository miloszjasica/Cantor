package com.milosz.cantor.infrastructure.schedulder;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class NbpRate {
    private String currency;
    private String code;
    private BigDecimal mid;

    public String getCurrency() {
        return currency;
    }

    public String getCode() {
        return code;
    }

    public BigDecimal getMid() {
        return mid;
    }
}
