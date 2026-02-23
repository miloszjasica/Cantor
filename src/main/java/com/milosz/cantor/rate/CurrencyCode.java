package com.milosz.cantor.rate;

import java.util.List;

public enum CurrencyCode {

    THB,
    USD,
    AUD,
    HKD,
    CAD,
    NZD,
    SGD,
    PLN,
    EUR,
    HUF,
    CHF,
    GBP,
    UAH,
    JPY,
    CZK,
    DKK,
    ISK,
    NOK,
    SEK,
    RON,
    TRY,
    ILS,
    CLP,
    PHP,
    MXN,
    ZAR,
    BRL,
    MYR,
    IDR,
    INR,
    KRW,
    CNY,
    XDR;

    public static List<CurrencyCode> basicTypes() {
        return List.of(CurrencyCode.USD, CurrencyCode.EUR, CurrencyCode.GBP, CurrencyCode.CHF);
    }

}
