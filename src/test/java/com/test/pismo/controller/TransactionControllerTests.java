package com.test.pismo.controller;

import com.test.pismo.controller.request.CreateAccountRequest;
import com.test.pismo.controller.request.CreateTransactionRequest;
import com.test.pismo.controller.response.AccountResponse;
import com.test.pismo.controller.response.ErrorResponse;
import com.test.pismo.controller.response.TransactionResponse;
import com.test.pismo.exception.NotFoundException;
import com.test.pismo.model.Account;
import com.test.pismo.model.OperationTypeEnum;
import com.test.pismo.model.Transaction;
import com.test.pismo.repository.AccountRepository;
import com.test.pismo.repository.OperationTypeRepository;
import com.test.pismo.repository.TransactionRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class TransactionControllerTests {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private OperationTypeRepository operationTypeRepository;

    @AfterEach
    public void after() {
        transactionRepository.deleteAll();
        accountRepository.deleteAll();
    }

    @Test
    public void createNewTransaction() throws Exception {
        var account = Account.builder().documentNumber(12345678900L).build();
        var createdAccount = accountRepository.save(account);

        var createTransactionRequest = new CreateTransactionRequest();
        createTransactionRequest.setAccountId(createdAccount.getId());
        createTransactionRequest.setOperationTypeId(OperationTypeEnum.A_VISTA.getOperation().getId());
        createTransactionRequest.setAmount(new BigDecimal(123));

        var request = new HttpEntity<>(createTransactionRequest);

        var response = restTemplate.postForEntity(new URL("http://localhost:" + port + "/transactions").toString(), request, TransactionResponse.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(createdAccount.getId(), response.getBody().getAccountId());
        assertEquals(OperationTypeEnum.A_VISTA.getOperation().getDescription(), response.getBody().getOperationType());
        assertEquals(new BigDecimal(-123).setScale(2, RoundingMode.FLOOR), response.getBody().getAmount());

    }


    @Test
    public void createNewTransactionShouldReturnOperationTypeError() throws Exception {
        var account = Account.builder().documentNumber(12345678900L).build();
        var createdAccount = accountRepository.save(account);

        var createTransactionRequest = new CreateTransactionRequest();
        createTransactionRequest.setAccountId(createdAccount.getId());
        createTransactionRequest.setOperationTypeId(5L);
        createTransactionRequest.setAmount(new BigDecimal(123));

        var request = new HttpEntity<>(createTransactionRequest);

        var response = restTemplate.postForEntity(new URL("http://localhost:" + port + "/transactions").toString(), request, ErrorResponse.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Invalid operation type option", response.getBody().getMessage());

    }

    @Test
    public void createNewTransactionShouldReturnAccountError() throws Exception {
        var createTransactionRequest = new CreateTransactionRequest();
        createTransactionRequest.setAccountId(5L);
        createTransactionRequest.setOperationTypeId(OperationTypeEnum.A_VISTA.getOperation().getId());
        createTransactionRequest.setAmount(new BigDecimal(123));

        var request = new HttpEntity<>(createTransactionRequest);

        var response = restTemplate.postForEntity(new URL("http://localhost:" + port + "/transactions").toString(), request, ErrorResponse.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Invalid account", response.getBody().getMessage());

    }


    @Test
    public void getTransactionById() throws Exception {
        var account = Account.builder().documentNumber(12345678900L).build();
        accountRepository.save(account);

        var operationType = operationTypeRepository.findById(OperationTypeEnum.A_VISTA.getOperation().getId()).get();

        var transaction = Transaction.builder().account(account).operationType(operationType).amount(new BigDecimal(123)).balance(new BigDecimal(123)).build();
        var createdTransaction = transactionRepository.save(transaction);

        var response = restTemplate.getForEntity(new URL("http://localhost:" + port + "/transactions/" + createdTransaction.getId()).toString(), TransactionResponse.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(transaction.getAccount().getId(), account.getId());
        assertEquals(transaction.getOperationType().getId(), operationType.getId());
        assertEquals(transaction.getAmount(), new BigDecimal(123));
    }

}
