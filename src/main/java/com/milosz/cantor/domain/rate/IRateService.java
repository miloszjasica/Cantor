package com.milosz.cantor.domain.rate;

import com.milosz.cantor.web.api.dto.ConversionResult;
import com.milosz.cantor.web.api.dto.ConvertRequest;
import com.milosz.cantor.web.api.dto.LatestRatesResponse;

import java.util.List;
import java.util.Optional;

public interface IRateService {

    Optional<LatestRatesResponse> getLatestRatesResponse(CurrencyCode base, List<CurrencyCode> symbolList);

    Optional<ConversionResult> convert(ConvertRequest request);

}
