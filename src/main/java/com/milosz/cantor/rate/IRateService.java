package com.milosz.cantor.rate;

import java.util.List;
import java.util.Optional;

public interface IRateService {

    Optional<LatestRatesResponse> getLatestRatesResponse(CurrencyCode base, List<CurrencyCode> symbolList);

    Optional<ConversionResult> convert(ConvertRequest request);

}
