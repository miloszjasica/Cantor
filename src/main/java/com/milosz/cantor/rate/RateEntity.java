package com.milosz.cantor.rate;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "rates")
class RateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "snapshot_id", nullable = false)
    private RateSnapshotEntity snapshot;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CurrencyCode currency;

    @Column(nullable = false, precision = 19, scale = 6)
    private BigDecimal rate;

    protected RateEntity() {
    }

    public RateEntity(CurrencyCode currency, BigDecimal rate, RateSnapshotEntity snapshot) {
        this.currency = currency;
        this.rate = rate;
        this.snapshot = snapshot;
    }

    public Long getId() {
        return id;
    }

    public RateSnapshotEntity getSnapshot() {
        return snapshot;
    }

    public void setSnapshot(RateSnapshotEntity snapshot) {
        this.snapshot = snapshot;
    }

    public CurrencyCode getCurrency() {
        return currency;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setCurrency(CurrencyCode currency) {
        this.currency = currency;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

}