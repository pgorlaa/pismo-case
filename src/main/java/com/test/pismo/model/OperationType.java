package com.test.pismo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Entity
@Table(name = "operation_types")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OperationType {

    @Id
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "debt", nullable = false)
    private boolean debt;


    public BigDecimal convertAmount(final BigDecimal amount) {
        var sign = debt ? BigDecimal.valueOf(-1D) : BigDecimal.valueOf(1D);
        return amount.setScale(2, RoundingMode.FLOOR).multiply(sign).setScale(2);
    }

}