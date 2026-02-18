package com.milosz.cantor.infrastructure.nbp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.milosz.cantor.domain.rate.CurrencyCode;

import java.math.BigDecimal;
import java.time.LocalDate;
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
        String url = "http://api.nbp.pl/api/exchangerates/tables/A/?format=json";
        
        try {        
            NbpTable[] response = restTemplate.getForObject(url, NbpTable[].class);
            
            if (response != null && response.length > 0) {
                LOGGER.info("Array: {}, data: {}", response[0].getNo(), response[0].getEffectiveDate());
                LOGGER.info("Rates count: {}", response[0].getRates().size());
                
                response[0].getRates().stream().limit(3).forEach(rate -> 
                    LOGGER.info("{}: {}", rate.getCode(), rate.getMid())
                );
                
                return mapToResponse(response[0]);
            }
        } catch (Exception e) {
            LOGGER.error("Fetching error: {}", e.getMessage());
            LOGGER.error("Fetching error: ", e);
        }
        
        LOGGER.warn("Mocked data used due to fetch failure");
        return getMockRates();
    }
    
    private NbpResponse mapToResponse(NbpTable table) {
        Map<CurrencyCode, BigDecimal> rates = table.getRates().stream()
            .collect(Collectors.toMap(
                rate -> CurrencyCode.valueOf(rate.getCode().toUpperCase()),
                NbpRate::getMid
            ));

        return new NbpResponse(
            LocalDate.parse(table.getEffectiveDate()),
            rates
        );
    }

    private NbpResponse getMockRates() {
        Map<CurrencyCode, BigDecimal> mockRates = Map.of(
            CurrencyCode.USD, new BigDecimal("3.98"),
            CurrencyCode.EUR, new BigDecimal("4.32"),
            CurrencyCode.GBP, new BigDecimal("5.12"),
            CurrencyCode.CHF, new BigDecimal("4.56")
        );

        return new NbpResponse(
            LocalDate.now(),
            mockRates
        );
    }


}
