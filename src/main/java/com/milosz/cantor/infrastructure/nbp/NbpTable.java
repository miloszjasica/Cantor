package com.milosz.cantor.infrastructure.nbp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class NbpTable {
    private String table;
    private String no;
    private String effectiveDate;
    private List<NbpRate> rates;
}

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
class NbpRate {
    public String currency;
    public String code;
    public BigDecimal mid;
}