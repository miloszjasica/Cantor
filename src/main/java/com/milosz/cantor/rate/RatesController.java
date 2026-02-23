package com.milosz.cantor.rate;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1/rates")
@SecurityRequirement(name = "bearerAuth")
public class RatesController {

    private final IRateService rateService;

    @Autowired
    public RatesController(IRateService rateService) {
        this.rateService = rateService;
    }

    @GetMapping("/latest")
    public ResponseEntity<LatestRatesResponse> getLatestRates(
            @RequestParam(defaultValue = "PLN") CurrencyCode base,
            @RequestParam(required = false) List<CurrencyCode> symbols) {
        final List<CurrencyCode> symbolList = getCurrencyCodes(symbols);
        return rateService.getLatestRatesResponse(base, symbolList)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.noContent().build());
    }

    @PostMapping("/convert")
    public ResponseEntity<ConversionResult> convert(@Valid @RequestBody ConvertRequest request) {
        return rateService.convert(request)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.noContent().build());
    }

    private static List<CurrencyCode> getCurrencyCodes(List<CurrencyCode> symbols) {
        return Objects.nonNull(symbols) && !symbols.isEmpty()
                ? symbols
                : CurrencyCode.basicTypes();
    }

}
