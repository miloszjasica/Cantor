package com.milosz.cantor.infrastructure.nbp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.milosz.cantor.domain.rate.CurrencyCode;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class NbpClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(NbpClient.class);

    private final RestTemplate restTemplate;

    public NbpClient() {
        this.restTemplate = new RestTemplate();
    }

    public NbpResponse fetchTodayRates() {
        String url = "http://api.nbp.pl/api/exchangerates/tables/A?format=json";
        try {
            NbpTable[] response = restTemplate.getForObject(url, NbpTable[].class);

            if (response != null && response.length > 0) {
                Map<CurrencyCode, BigDecimal> rates = response[0].getRates().stream()
                    .collect(Collectors.toMap(
                        r -> {
                            try {
                                return CurrencyCode.valueOf(r.getCode().toUpperCase());
                            } catch (IllegalArgumentException e) {
                                LOGGER.warn("Unknown API currency: {}", r.getCode());
                                return null;
                            }
                        },
                        NbpRate::getMid,
                        (r1, r2) -> r1
                    ));
                rates.values().removeIf(v -> v == null);
                return new NbpResponse(LocalDate.parse(response[0].getEffectiveDate()), rates);
            }
        } catch (Exception e) {
            LOGGER.error("Error downloading NBP rates: {}", e.getMessage());
        }
        return getMockRates();
    }

    private NbpResponse getMockRates() {
        Map<CurrencyCode, BigDecimal> mockRates = new HashMap<>();
        mockRates.put(CurrencyCode.USD, new BigDecimal("3.98"));
        mockRates.put(CurrencyCode.EUR, new BigDecimal("4.32"));
        mockRates.put(CurrencyCode.GBP, new BigDecimal("5.12"));
        mockRates.put(CurrencyCode.CHF, new BigDecimal("4.56"));
        return new NbpResponse(LocalDate.now(), mockRates);
    }
}