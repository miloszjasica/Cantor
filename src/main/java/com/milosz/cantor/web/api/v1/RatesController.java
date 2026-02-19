package com.milosz.cantor.web.api.v1;

import com.milosz.cantor.domain.rate.CurrencyCode;
import com.milosz.cantor.domain.rate.RateService;
import com.milosz.cantor.domain.rate.RateSnapshot;
import com.milosz.cantor.web.api.dto.ConversionResult;
import com.milosz.cantor.web.api.dto.ConvertRequest;
import com.milosz.cantor.web.api.dto.LatestRatesResponse;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/rates")
public class RatesController {

    private final RateService rateService;

    public RatesController(RateService rateService) {
        this.rateService = rateService;
    }

    @GetMapping("/latest")
    public ResponseEntity<LatestRatesResponse> getLatestRates(
            @RequestParam(defaultValue = "PLN") CurrencyCode base,
            @RequestParam(required = false) List<CurrencyCode> symbols) {

        RateSnapshot snapshot = rateService.getLatestRates();

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
                        .map(rate -> new LatestRatesResponse.RateDto(
                                rate.getCurrency().name(),
                                rate.getRate().toPlainString()
                        ))
                        .collect(Collectors.toList());

        LatestRatesResponse response =
                new LatestRatesResponse(
                        base.name(),
                        snapshot.getEffectiveDate()
                                .atStartOfDay(ZoneId.systemDefault())
                                .toInstant(),
                        snapshot.getSource(),
                        filteredRates
                );

        return ResponseEntity.ok(response);
    }

    @PostMapping("/convert")
    public ConversionResult convert(@Valid @RequestBody ConvertRequest request) {
        return rateService.convert(
                request.fromCurrency(),
                request.toCurrency(),
                request.amount()
        );
    }
}
