package com.milosz.cantor.domain.rate;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "rates")
public class Rate {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @ManyToOne
    @JoinColumn(name = "snapshot_id", nullable = false)
    private RateSnapshot snapshot;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CurrencyCode currency;
    
    @Column(nullable = false, precision = 19, scale = 6)
    private BigDecimal rate;

    protected Rate() {
    }

    public Rate(CurrencyCode currency, BigDecimal rate, RateSnapshot snapshot) {
        this.currency = currency;
        this.rate = rate;
        this.snapshot = snapshot;
    }

    public UUID getId() {
        return id;
    }

     public RateSnapshot getSnapshot() {
        return snapshot;
    }

    public void setSnapshot(RateSnapshot snapshot) {
        this.snapshot = snapshot;
    }

    public CurrencyCode getCurrency() {
        return currency;
    }

    public BigDecimal getRate() {
        return rate;
    }

}