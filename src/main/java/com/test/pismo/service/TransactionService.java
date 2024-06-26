package com.test.pismo.service;

import com.test.pismo.controller.request.CreateTransactionRequest;
import com.test.pismo.controller.response.TransactionResponse;
import com.test.pismo.exception.NotFoundException;
import com.test.pismo.mapper.TransactionMapper;
import com.test.pismo.model.OperationTypeEnum;
import com.test.pismo.model.Transaction;
import com.test.pismo.repository.AccountRepository;
import com.test.pismo.repository.OperationTypeRepository;
import com.test.pismo.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

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

    public TransactionResponse createTransaction(CreateTransactionRequest createTransactionRequest) {
        var operationType = operationTypeRepository.findById(createTransactionRequest.getOperationTypeId()).orElseThrow(() -> new NotFoundException("Invalid operation type option"));

        var account = accountRepository.findById(createTransactionRequest.getAccountId()).orElseThrow(() -> new NotFoundException("Invalid account"));

        BigDecimal balance = createTransactionRequest.getAmount().setScale(2, RoundingMode.FLOOR);

        if (Objects.equals(createTransactionRequest.getOperationTypeId(), OperationTypeEnum.PAGAMENTO.getOperation().getId())) {
            var transactions = transactionRepository.findTransactionsByAccountId(account.getId());

            balance = updateBalance(transactions, balance);
        }

        var transaction = transactionMapper.toModel(createTransactionRequest, operationType, account);
        transaction.setBalance(operationType.convertAmount(balance));

        return transactionMapper.toResponse(transactionRepository.save(transaction));
    }

    //    simplificando a lógica
    private BigDecimal updateBalance(List<Transaction> transactions, BigDecimal balance) {
        for (Transaction transaction : transactions) {
            if (balance.compareTo(BigDecimal.ZERO) <= 0) {
                break;
            }

            balance = balance.add(transaction.getBalance().setScale(2, RoundingMode.FLOOR));
            transaction.setBalance(balance.min(BigDecimal.ZERO));
            transactionRepository.setNewBalance(transaction.getBalance(), transaction.getId());
        }

        return balance.max(BigDecimal.ZERO);
    }
}
