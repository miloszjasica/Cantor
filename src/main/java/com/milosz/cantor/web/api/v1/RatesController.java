package com.milosz.cantor.web.api.v1;

import com.milosz.cantor.domain.rate.RateService;
import com.milosz.cantor.web.api.dto.LatestRatesResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.ZoneId;
import java.util.List;

@RestController
@RequestMapping("/api/v1/rates")
@RequiredArgsConstructor
public class RatesController {
    
    private final RateService rateService;
    
    @GetMapping("/latest")
    public ResponseEntity<LatestRatesResponse> getLatestRates(
            @RequestParam(defaultValue = "PLN") String base,
            @RequestParam(required = false) String symbols) {
        
        var snapshot = rateService.getLatestSnapshot();
        
        List<String> symbolList = symbols != null ? 
            List.of(symbols.split(",")) : 
            List.of("USD", "EUR", "GBP", "CHF");
        
        var filteredRates = snapshot.getRates().stream()
            .filter(r -> symbolList.contains(r.getCurrency()))
            .map(r -> LatestRatesResponse.RateDto.builder()
                .symbol(r.getCurrency())
                .rate(r.getRate().toString())
                .build())
            .toList();
        
        var response = LatestRatesResponse.builder()
            .base(base)
            .asOf(snapshot.getEffectiveDate()
                .atStartOfDay(ZoneId.systemDefault())
                .toInstant())
            .source(snapshot.getSource())
            .rates(filteredRates)
            .build();
        
        return ResponseEntity.ok(response);
    }
}