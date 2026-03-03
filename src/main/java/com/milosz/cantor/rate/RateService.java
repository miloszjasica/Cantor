package com.milosz.cantor.rate;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

@Service
class RateService implements IRateService {

    private final RateSnapshotRepository snapshotRepository;

    public RateService(RateSnapshotRepository snapshotRepository) {
        this.snapshotRepository = snapshotRepository;
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<LatestRatesResponse> getLatestRatesResponse(CurrencyCode base, List<CurrencyCode> symbolList) {
        return snapshotRepository.findFirstByOrderByEffectiveDateDesc()
                .map(snapshot -> prepareLatestResponse(base, symbolList, snapshot));
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<ConversionResult> convert(ConvertRequest request) {
        return snapshotRepository.findFirstByOrderByEffectiveDateDesc()
                .map(snapshot -> getConversionResult(request.fromCurrency(), request.toCurrency(), request.amount(), snapshot));
    }

    private static ConversionResult getConversionResult(CurrencyCode from, CurrencyCode to, BigDecimal amount, RateSnapshotEntity snapshot) {
        if (from == to) {
            return ConversionResult.sameCurrency(from, amount, snapshot);
        }

        BigDecimal rate = snapshot.findRate(from, to);

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

    private static LatestRatesResponse prepareLatestResponse(CurrencyCode base, List<CurrencyCode> symbolList, RateSnapshotEntity snapshot) {
        List<LatestRatesResponse.RateDto> filteredRates = snapshot.getLatest(symbolList);
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
