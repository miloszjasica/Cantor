package com.milosz.cantor.web.api.v1;

import com.milosz.cantor.domain.rate.CurrencyCode;
import com.milosz.cantor.domain.rate.RateService;
import com.milosz.cantor.domain.rate.RateSnapshot;
import com.milosz.cantor.web.api.dto.LatestRatesResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.ZoneId;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/rates")
@RequiredArgsConstructor
public class RatesController {

    private final RateService rateService;

    @GetMapping("/latest")
    public ResponseEntity<LatestRatesResponse> getLatestRates(
            @RequestParam(defaultValue = "PLN") CurrencyCode base,
            @RequestParam(required = false) List<CurrencyCode> symbols) {

        RateSnapshot snapshot = rateService.getLatestSnapshot();

        List<CurrencyCode> symbolList;

        if (symbols != null && !symbols.isEmpty()) {
            symbolList = symbols;
        } else {
            symbolList = List.of(
                    CurrencyCode.USD,
                    CurrencyCode.EUR,
                    CurrencyCode.GBP,
                    CurrencyCode.CHF
            );
        }

        List<LatestRatesResponse.RateDto> filteredRates =
                snapshot.getRates().stream()
                        .filter(rate -> symbolList.contains(rate.getCurrency()))
                        .map(rate -> LatestRatesResponse.RateDto.builder()
                                .symbol(rate.getCurrency().name())
                                .rate(rate.getRate().toString())
                                .build())
                        .collect(Collectors.toList());

        LatestRatesResponse response =
                LatestRatesResponse.builder()
                        .base(base.name())
                        .asOf(snapshot.getEffectiveDate()
                                .atStartOfDay(ZoneId.systemDefault())
                                .toInstant())
                        .source(snapshot.getSource())
                        .rates(filteredRates)
                        .build();

        return ResponseEntity.ok(response);
    }
}
