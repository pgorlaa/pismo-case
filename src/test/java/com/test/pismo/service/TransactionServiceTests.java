package com.test.pismo.service;

import com.test.pismo.PismoApplication;
import com.test.pismo.config.H2TestProfileConfig;
import com.test.pismo.controller.request.CreateAccountRequest;
import com.test.pismo.controller.request.CreateTransactionRequest;
import com.test.pismo.controller.response.AccountResponse;
import com.test.pismo.controller.response.TransactionResponse;
import com.test.pismo.exception.DuplicatedException;
import com.test.pismo.exception.NotFoundException;
import com.test.pismo.mapper.TransactionMapper;
import com.test.pismo.model.Account;
import com.test.pismo.model.OperationType;
import com.test.pismo.model.OperationTypeEnum;
import com.test.pismo.model.Transaction;
import com.test.pismo.repository.AccountRepository;
import com.test.pismo.repository.OperationTypeRepository;
import com.test.pismo.repository.TransactionRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(classes = {PismoApplication.class, H2TestProfileConfig.class})
@ActiveProfiles("test")
public class TransactionServiceTests {

    @InjectMocks
    private TransactionService transactionService;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private OperationTypeRepository operationTypeRepository;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private TransactionMapper transactionMapper;

    @Test
    public void createNewAccount() {
        var createTransactionRequest = new CreateTransactionRequest(1L, 1L, new BigDecimal(123));

        var operationType = OperationTypeEnum.A_VISTA.getOperation();
        var account = Account.builder().documentNumber(123456789L).id(1L).build();
        var transaction = Transaction.builder()
                .operationType(operationType)
                .account(account)
                .amount(operationType.convertAmount(createTransactionRequest.getAmount()))
                .eventDate(LocalDateTime.now())
                .build();
        var response = TransactionResponse.builder().id(1L).accountId(1L).operationType(operationType.getDescription()).amount(transaction.getAmount()).build();

        Mockito.when(transactionMapper.toModel(createTransactionRequest, operationType, account)).thenReturn(transaction);
        Mockito.when(transactionRepository.save(transaction)).thenReturn(transaction);
        Mockito.when(operationTypeRepository.findById(1L)).thenReturn(Optional.of(operationType));
        Mockito.when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        Mockito.when(transactionMapper.toResponse(transaction)).thenReturn(response);

        var newTransaction = transactionService.createTransaction(createTransactionRequest);

        Assertions.assertEquals(newTransaction.getAmount(), transaction.getAmount());

    }

    @Test
    public void createNewAccountWithUnknownOperationTypeShouldThrowError() {
        var createTransactionRequest = new CreateTransactionRequest(1L, 1L, new BigDecimal(123));

        NotFoundException notFoundException = assertThrows(NotFoundException.class,
                () -> transactionService.createTransaction(createTransactionRequest));


        assertEquals("Invalid operation type option", notFoundException.getMessage());

    }

    @Test
    public void createNewAccountWithUnknownAccountShouldThrowError() {
        var createTransactionRequest = new CreateTransactionRequest(1L, 1L, new BigDecimal(123));
        var operationType = OperationTypeEnum.A_VISTA.getOperation();
        Mockito.when(operationTypeRepository.findById(1L)).thenReturn(Optional.of(operationType));

        NotFoundException notFoundException = assertThrows(NotFoundException.class,
                () -> transactionService.createTransaction(createTransactionRequest));


        assertEquals("Invalid account", notFoundException.getMessage());

    }


    @Test
    public void getTransactionById() {

        var operationType = OperationTypeEnum.A_VISTA.getOperation();
        var account = Account.builder().documentNumber(123456789L).id(1L).build();
        var transaction = Transaction.builder()
                .operationType(operationType)
                .account(account)
                .amount(operationType.convertAmount(new BigDecimal(123)))
                .eventDate(LocalDateTime.now())
                .build();
        var response = TransactionResponse.builder().id(1L).accountId(1L).operationType(operationType.getDescription()).amount(transaction.getAmount()).build();

        Mockito.when(transactionRepository.findById(1L)).thenReturn(Optional.of(transaction));
        Mockito.when(transactionMapper.toResponse(transaction)).thenReturn(response);

        var trasactionResponse = transactionService.getTransactionById(1L);

        Assertions.assertEquals(trasactionResponse.getAmount(), transaction.getAmount());

    }

    @Test
    public void getTransactionByIdShouldThrowNotFound() {

        NotFoundException notFoundException = assertThrows(NotFoundException.class,
                () -> transactionService.getTransactionById(1L));


        assertEquals("Transaction not found", notFoundException.getMessage());

    }
}
