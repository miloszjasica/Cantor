package com.milosz.cantor.nbp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class NbpClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(NbpClient.class);

    private static final String NBP_URL = "http://api.nbp.pl/api/exchangerates/tables/A?format=json";

    private final RestTemplate restTemplate;

    @Autowired
    public NbpClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public NbpResponse fetchTodayRates() {
        try {
            NbpTable[] response = restTemplate.getForObject(NBP_URL, NbpTable[].class);

            if (response != null && response.length > 0) {
                Map<NbpCurrencyCode, BigDecimal> rates = response[0].rates().stream()
                        .collect(Collectors.toMap(
                                r -> {
                                    try {
                                        return NbpCurrencyCode.valueOf(r.code().toUpperCase());
                                    } catch (IllegalArgumentException e) {
                                        LOGGER.warn("Unknown API currency: {}", r.code());
                                        return null;
                                    }
                                },
                                NbpRate::mid,
                                (r1, r2) -> r1
                        ));
                rates.values().removeIf(v -> v == null);
                return new NbpResponse(LocalDate.parse(response[0].effectiveDate()), rates);
            }
        } catch (Exception e) {
            LOGGER.error("Error downloading NBP rates: {}", e.getMessage());
        }
        return getMockRates();
    }

    private NbpResponse getMockRates() {
        Map<NbpCurrencyCode, BigDecimal> mockRates = new HashMap<>();
        mockRates.put(NbpCurrencyCode.USD, new BigDecimal("3.98"));
        mockRates.put(NbpCurrencyCode.EUR, new BigDecimal("4.32"));
        mockRates.put(NbpCurrencyCode.GBP, new BigDecimal("5.12"));
        mockRates.put(NbpCurrencyCode.CHF, new BigDecimal("4.56"));
        return new NbpResponse(LocalDate.now(), mockRates);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record NbpTable(String table, String no, String effectiveDate, List<NbpRate> rates) { }

    @JsonIgnoreProperties(ignoreUnknown = true)
    record NbpRate(String currency, String code, BigDecimal mid) { }

}