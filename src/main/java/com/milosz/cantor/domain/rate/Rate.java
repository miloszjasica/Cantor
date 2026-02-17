package com.milosz.cantor.domain.rate;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "rates")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Rate {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @ManyToOne
    @JoinColumn(name = "snapshot_id", nullable = false)
    private RateSnapshot snapshot;
    
    @Column(nullable = false)
    private String currency;
    
    @Column(nullable = false, precision = 19, scale = 6)
    private BigDecimal rate;
}