package com.milosz.cantor.domain.rate;

import jakarta.persistence.*;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "rate_snapshots")
public class RateSnapshot {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CurrencyCode baseCurrency;

    @Column(nullable = false)
    private String source;

    @Column(nullable = false)
    private Instant fetchedAt;

    @Column(nullable = false)
    private LocalDate effectiveDate;

    @OneToMany(
            mappedBy = "snapshot",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            orphanRemoval = true
    )
    private List<Rate> rates = new ArrayList<>();

    protected RateSnapshot() {
    }

    public RateSnapshot(
            CurrencyCode baseCurrency,
            String source,
            Instant fetchedAt,
            LocalDate effectiveDate
    ) {
        this.baseCurrency = baseCurrency;
        this.source = source;
        this.fetchedAt = fetchedAt;
        this.effectiveDate = effectiveDate;
    }

    public UUID getId() {
        return id;
    }

    public CurrencyCode getBaseCurrency() {
        return baseCurrency;
    }

    public String getSource() {
        return source;
    }

    public Instant getFetchedAt() {
        return fetchedAt;
    }

    public LocalDate getEffectiveDate() {
        return effectiveDate;
    }

    public List<Rate> getRates() {
        return rates;
    }

    public void addRate(Rate rate) {
        rates.add(rate);
        rate.setSnapshot(this);
    }

    public void removeRate(Rate rate) {
        rates.remove(rate);
        rate.setSnapshot(null);
    }
}
