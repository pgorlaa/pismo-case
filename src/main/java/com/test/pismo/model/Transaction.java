package com.test.pismo.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "account_id", updatable = false, nullable = false)
    private Account account;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "operation_type_id", updatable = false, nullable = false)
    private OperationType operationType;

    @Column(name = "amount", updatable = false, nullable = false)
    private BigDecimal amount;

    @Column(name = "balance", updatable = false, nullable = false)
    private BigDecimal balance;

    @Column(name = "event_date")
    private LocalDateTime eventDate;


}