package com.milosz.cantor.domain.rate;

import com.milosz.cantor.infrastructure.nbp.NbpClient;
import com.milosz.cantor.infrastructure.nbp.NbpResponse;
import com.milosz.cantor.web.api.dto.ConversionResult;
import com.milosz.cantor.web.api.dto.LatestRatesResponse;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RateService {

    private final RateSnapshotRepository snapshotRepository;
    private final NbpClient nbpClient;

    public RateService(RateSnapshotRepository snapshotRepository, NbpClient nbpClient) {
        this.snapshotRepository = snapshotRepository;
        this.nbpClient = nbpClient;
    }

    public RateSnapshot getLatestRates() {
        return snapshotRepository.findFirstByOrderByEffectiveDateDesc().orElse(null);
    }

    @Transactional
    public RateSnapshot fetchRatesFromNbp() {
        LocalDate today = LocalDate.now();

        if (snapshotRepository.existsByEffectiveDate(today)) {
            return snapshotRepository.findFirstByOrderByEffectiveDateDesc().orElse(null);
        }

        NbpResponse nbpResponse = nbpClient.fetchTodayRates();

        RateSnapshot snapshot = new RateSnapshot(
            CurrencyCode.PLN,
            "NBP",
            Instant.now(),
            nbpResponse.getEffectiveDate()
        );
    
        Map<CurrencyCode, BigDecimal> rawRates = nbpResponse.getRates();


        rawRates.forEach((currencyCode, value) -> {
            Rate rate = new Rate(currencyCode, value, snapshot);
            snapshot.addRate(rate);
        });


        return snapshotRepository.save(snapshot);
    }

    @Transactional(readOnly = true)
    public ConversionResult convert(
            CurrencyCode from,
            CurrencyCode to,
            BigDecimal amount
    ) {
        RateSnapshot snapshot = getLatestRates();

        if (snapshot == null) {
            throw new IllegalStateException("Currency rates not available. Please try again later.");
        }

        if (from == to) {
            return ConversionResult.sameCurrency(from, amount, snapshot);
        }

        BigDecimal rate = findRate(snapshot, from, to);

        BigDecimal result;

        if (from == CurrencyCode.PLN) {
            result = amount.divide(rate, 6, RoundingMode.HALF_UP);
        } else if (to == CurrencyCode.PLN) {
            result = amount.multiply(rate);
        } else {
            throw new IllegalArgumentException("Conversion between non-PLN currencies is not supported.");
        }

        BigDecimal rounded = result.setScale(2, RoundingMode.HALF_UP);

        return ConversionResult.of(
                from,
                to,
                amount,
                rounded,
                rate,
                snapshot
        );
    }

    private BigDecimal findRate(RateSnapshot snapshot,
                                CurrencyCode from,
                                CurrencyCode to) {

        CurrencyCode target = from == CurrencyCode.PLN ? to : from;

        return snapshot.getRates().stream()
                .filter(r -> r.getCurrency() == target)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Brak kursu dla " + target))
                .getRate();
    }

    public LatestRatesResponse getLatestRates(List<CurrencyCode> symbolList, CurrencyCode base) {
    final RateSnapshot snapshot = snapshotRepository.findFirstByOrderByEffectiveDateDesc().orElse(null);
        List<LatestRatesResponse.RateDto> filteredRates =
                snapshot.getRates().stream()
                        .filter(rate -> symbolList.contains(rate.getCurrency()))
                        .map(rate -> new LatestRatesResponse.RateDto(
                                rate.getCurrency().name(),
                                rate.getRate().toPlainString()
                        ))
                        .collect(Collectors.toList());

        return new LatestRatesResponse(
                        base.name(),
                        snapshot.getEffectiveDate()
                                .atStartOfDay(ZoneId.systemDefault())
                                .toInstant(),
                        snapshot.getSource(),
                        filteredRates
                );
    }
}
