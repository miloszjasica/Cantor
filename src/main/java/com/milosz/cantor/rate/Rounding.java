package com.milosz.cantor.rate;

import java.math.RoundingMode;

public record Rounding(
        int scale,
        RoundingMode mode
) {}

