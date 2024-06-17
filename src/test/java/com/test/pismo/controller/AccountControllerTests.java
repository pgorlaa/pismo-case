package com.test.pismo.controller;

import com.test.pismo.controller.request.CreateAccountRequest;
import com.test.pismo.controller.response.AccountResponse;
import com.test.pismo.controller.response.ErrorResponse;
import com.test.pismo.model.Account;
import com.test.pismo.repository.AccountRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Commit;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.transaction.AfterTransaction;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class AccountControllerTests {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private AccountRepository accountRepository;

    @AfterEach
    public void after() {
        accountRepository.deleteAll();
    }

    @Test
    public void createNewAccount() throws Exception {
        var createAccountRequest = new CreateAccountRequest();
        createAccountRequest.setDocumentNumber(12345678900L);

        var request = new HttpEntity<>(createAccountRequest);

        var response = restTemplate.postForEntity(new URL("http://localhost:" + port + "/accounts").toString(), request, AccountResponse.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(12345678900L, response.getBody().getDocumentNumber());

    }

    @Test
    public void createNewAccountShouldReturnError() throws Exception {
        var account = Account.builder().documentNumber(12345678900L).build();
        accountRepository.save(account);

        var createAccountRequest = new CreateAccountRequest();
        createAccountRequest.setDocumentNumber(12345678900L);

        var request = new HttpEntity<>(createAccountRequest);

        var response = restTemplate.postForEntity(new URL("http://localhost:" + port + "/accounts").toString(), request, ErrorResponse.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Account already exists", response.getBody().getMessage());

    }

    @Test
    public void getAccountById() throws Exception {
        var account = Account.builder().documentNumber(12345678900L).build();
        var createdAccount = accountRepository.save(account);

        var response = restTemplate.getForEntity(new URL("http://localhost:" + port + "/accounts/" + createdAccount.getId()).toString(), AccountResponse.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(account.getDocumentNumber(), response.getBody().getDocumentNumber());

    }

}
