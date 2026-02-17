package com.milosz.cantor.infrastructure.nbp;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

@Data
@Builder
public class NbpResponse {
    private LocalDate effectiveDate;
    private Map<String, BigDecimal> rates;
}