package com.milosz.cantor.nbp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.math.BigDecimal;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class NbpTable {
    private String table;
    private String no;
    private String effectiveDate;
    private List<NbpRate> rates;

    public String getTable() {
        return table;
    }

    public String getNo() {
        return no;
    }

    public String getEffectiveDate() {
        return effectiveDate;
    }

    public List<NbpRate> getRates() {
        return rates;
    }
}

@JsonIgnoreProperties(ignoreUnknown = true)
class NbpRate {
    public String currency;
    public String code;
    public BigDecimal mid;

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