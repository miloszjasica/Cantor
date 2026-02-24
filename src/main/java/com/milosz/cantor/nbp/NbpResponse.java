package com.milosz.cantor.nbp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

public record NbpResponse(LocalDate effectiveDate, Map<NbpCurrencyCode, BigDecimal> rates) {

}