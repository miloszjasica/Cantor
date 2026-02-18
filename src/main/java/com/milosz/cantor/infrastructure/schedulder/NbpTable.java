package com.milosz.cantor.infrastructure.schedulder;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
