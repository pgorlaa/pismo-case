package com.test.pismo.repository;

import com.test.pismo.model.Transaction;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface TransactionRepository extends CrudRepository<Transaction, Long> {
    @Query("SELECT tr from Transaction tr where tr.account.id = :accountId AND tr.operationType.id <> 4 AND tr.balance < 0")
    List<Transaction> findTransactionsByAccountId(Long accountId);

    @Modifying
    @Query("UPDATE Transaction tr set tr.balance = :newBalance WHERE tr.id = :transactionId")
    @Transactional
    void setNewBalance(BigDecimal newBalance, Long transactionId);

}