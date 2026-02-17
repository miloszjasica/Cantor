package com.milosz.cantor.domain.rate;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "rate_snapshots")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RateSnapshot {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @Column(nullable = false)
    private String baseCurrency;
    
    @Column(nullable = false)
    private String source;
    
    @Column(nullable = false)
    private Instant fetchedAt;
    
    @Column(nullable = false)
    private LocalDate effectiveDate;
    
    @OneToMany(mappedBy = "snapshot", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @Builder.Default
    private List<Rate> rates = new ArrayList<>();
}