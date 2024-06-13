package com.test.pismo.mapper;

import com.test.pismo.controller.request.CreateTransactionRequest;
import com.test.pismo.controller.response.TransactionResponse;
import com.test.pismo.model.Account;
import com.test.pismo.model.OperationType;
import com.test.pismo.model.Transaction;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class TransactionMapper {

    @Autowired
    private ModelMapper modelMapper;

    public TransactionResponse toResponse(Transaction transaction) {
        var transactionResponse = modelMapper.map(transaction, TransactionResponse.class);

        transactionResponse.setOperationType(transaction.getOperationType().getDescription());

        return transactionResponse;
    }

    public Transaction toModel(CreateTransactionRequest createTransactionRequest, OperationType operationType, Account account) {
        var transaction = modelMapper.map(createTransactionRequest, Transaction.class);
        transaction.setEventDate(LocalDateTime.now());
        transaction.setOperationType(operationType);
        transaction.setAccount(account);
        transaction.setAmount(operationType.convertAmount(createTransactionRequest.getAmount()));
        return transaction;
    }

}
