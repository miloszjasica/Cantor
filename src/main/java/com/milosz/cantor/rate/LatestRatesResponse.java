package com.milosz.cantor.rate;

import java.time.Instant;
import java.util.List;

record LatestRatesResponse(String base, Instant asOf, String source, List<RateDto> rates) {

    public record RateDto(String symbol, String rate) { }

}