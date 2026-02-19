package com.milosz.cantor.domain.rate;

import java.math.RoundingMode;

public record Rounding(
        int scale,
        RoundingMode mode
) {}

