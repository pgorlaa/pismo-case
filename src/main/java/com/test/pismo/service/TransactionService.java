package com.test.pismo.service;

import com.test.pismo.controller.request.CreateTransactionRequest;
import com.test.pismo.controller.response.TransactionResponse;
import com.test.pismo.mapper.TransactionMapper;
import com.test.pismo.model.Transaction;
import com.test.pismo.repository.AccountRepository;
import com.test.pismo.repository.OperationTypeRepository;
import com.test.pismo.repository.TransactionRepository;
import com.test.pismo.exception.NotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;

    private final OperationTypeRepository operationTypeRepository;
    private final AccountRepository accountRepository;

    private final TransactionMapper transactionMapper;

    @Autowired
    public TransactionService(final TransactionRepository transactionRepository, final OperationTypeRepository operationTypeRepository, AccountRepository accountRepository, final TransactionMapper transactionMapper) {
        this.transactionRepository = transactionRepository;
        this.operationTypeRepository = operationTypeRepository;
        this.accountRepository = accountRepository;
        this.transactionMapper = transactionMapper;
    }

    public TransactionResponse getTransactionById(Long accountId) {
        var account = transactionRepository.findById(accountId).orElseThrow(() -> new NotFoundException("Transaction not found"));

        return transactionMapper.toResponse(account);
    }

    @Transactional
    public TransactionResponse createTransaction(CreateTransactionRequest createTransactionRequest) {
        var operationType = operationTypeRepository.findById(createTransactionRequest.getOperationTypeId()).orElseThrow(() -> new NotFoundException("Invalid operation type option"));

        var account = accountRepository.findById(createTransactionRequest.getAccountId()).orElseThrow(() -> new NotFoundException("Invalid account"));

        var transaction = transactionMapper.toModel(createTransactionRequest, operationType, account);

        return transactionMapper.toResponse(transactionRepository.save(transaction));
    }
}
