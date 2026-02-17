package com.milosz.cantor.web.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.Instant;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LatestRatesResponse {
    private String base;
    private Instant asOf;
    private String source;
    private List<RateDto> rates;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RateDto {
        private String symbol;
        private String rate;
    }
}