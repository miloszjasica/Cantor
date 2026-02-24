package com.milosz.cantor.rate;

import com.milosz.cantor.nbp.NbpCurrencyCode;

import java.util.List;
import java.util.function.Function;

enum CurrencyCode {

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

    public static Function<NbpCurrencyCode, CurrencyCode> nbpCurrencyMapper() {
        return x -> switch (x) {
            case THB -> CurrencyCode.THB;
            case USD -> CurrencyCode.USD;
            case AUD -> CurrencyCode.AUD;
            case HKD -> CurrencyCode.HKD;
            case CAD -> CurrencyCode.CAD;
            case BRL -> CurrencyCode.BRL;
            case CHF -> CurrencyCode.CHF;
            case CLP -> CurrencyCode.CLP;
            case CNY -> CurrencyCode.CNY;
            case CZK -> CurrencyCode.CZK;
            case DKK -> CurrencyCode.DKK;
            case NZD -> CurrencyCode.NZD;
            case SGD -> CurrencyCode.SGD;
            case PLN -> CurrencyCode.PLN;
            case EUR -> CurrencyCode.EUR;
            case GBP -> CurrencyCode.GBP;
            case HUF -> CurrencyCode.HUF;
            case UAH -> CurrencyCode.UAH;
            case JPY -> CurrencyCode.JPY;
            case ISK -> CurrencyCode.ISK;
            case NOK -> CurrencyCode.NOK;
            case SEK -> CurrencyCode.SEK;
            case RON -> CurrencyCode.RON;
            case TRY -> CurrencyCode.TRY;
            case ILS -> CurrencyCode.ILS;
            case PHP -> CurrencyCode.PHP;
            case MXN -> CurrencyCode.MXN;
            case ZAR -> CurrencyCode.ZAR;
            case MYR -> CurrencyCode.MYR;
            case IDR -> CurrencyCode.IDR;
            case INR -> CurrencyCode.INR;
            case KRW -> CurrencyCode.KRW;
            case XDR -> CurrencyCode.XDR;
        };
    }

}
