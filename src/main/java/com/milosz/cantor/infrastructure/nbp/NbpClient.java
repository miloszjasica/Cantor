package com.milosz.cantor.infrastructure.nbp;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Slf4j
public class NbpClient {
    
    private final RestTemplate restTemplate;
    
    public NbpClient() {
        this.restTemplate = new RestTemplate();
    }
    
    public NbpResponse fetchTodayRates() {
        String url = "http://api.nbp.pl/api/exchangerates/tables/A/?format=json";
        
        try {
            log.info("Fetching exchange rates from NBP...");
            log.info("URL: {}", url);
            
            NbpTable[] response = restTemplate.getForObject(url, NbpTable[].class);
            log.info("Response from NBP: {}", response != null ? "received" : "null");
            
            if (response != null && response.length > 0) {
                log.info("Array: {}, data: {}", response[0].getNo(), response[0].getEffectiveDate());
                log.info("Rates count: {}", response[0].getRates().size());
                
                response[0].getRates().stream().limit(3).forEach(rate -> 
                    log.info("{}: {}", rate.getCode(), rate.getMid())
                );
                
                return mapToResponse(response[0]);
            }
        } catch (Exception e) {
            log.error("Fetching error: {}", e.getMessage());
            e.printStackTrace();
        }
        
        log.warn("Mocked data used due to fetch failure");
        return getMockRates();
    }
    
    private NbpResponse mapToResponse(NbpTable table) {
        Map<String, BigDecimal> rates = table.getRates().stream()
            .collect(Collectors.toMap(
                NbpRate::getCode,
                NbpRate::getMid
            ));
        
        return NbpResponse.builder()
            .effectiveDate(LocalDate.parse(table.getEffectiveDate()))
            .rates(rates)
            .build();
    }
    
    private NbpResponse getMockRates() {
        return NbpResponse.builder()
            .effectiveDate(LocalDate.now())
            .rates(Map.of(
                "USD", new BigDecimal("3.98"),
                "EUR", new BigDecimal("4.32"),
                "GBP", new BigDecimal("5.12"),
                "CHF", new BigDecimal("4.56")
            ))
            .build();
    }
}