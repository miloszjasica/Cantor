package com.milosz.cantor.rate;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "rate_snapshots")
class RateSnapshotEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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
            fetch = FetchType.EAGER,
            orphanRemoval = true
    )
    private List<RateEntity> rateEntities = new ArrayList<>();

    public RateSnapshotEntity() {
    }

    public RateSnapshotEntity(
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

    public Long getId() {
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

    public List<RateEntity> getRates() {
        return rateEntities;
    }

    public void addRate(RateEntity rateEntity) {
        rateEntities.add(rateEntity);
        rateEntity.setSnapshot(this);
    }

    public void removeRate(RateEntity rateEntity) {
        rateEntities.remove(rateEntity);
        rateEntity.setSnapshot(null);
    }

    public List<LatestRatesResponse.RateDto> getLatest(List<CurrencyCode> symbolList) {
        return rateEntities.stream()
                .filter(rateEntity -> symbolList.contains(rateEntity.getCurrency()))
                .map(rateEntity -> new LatestRatesResponse.RateDto(
                        rateEntity.getCurrency().name(),
                        rateEntity.getRate().toPlainString()
                ))
                .collect(Collectors.toList());
    }

    public BigDecimal findRate(CurrencyCode from, CurrencyCode to) {
        CurrencyCode target = from == CurrencyCode.PLN ? to : from;
        return rateEntities.stream()
                .filter(r -> r.getCurrency() == target)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown rate for " + target))
                .getRate();
    }

}
