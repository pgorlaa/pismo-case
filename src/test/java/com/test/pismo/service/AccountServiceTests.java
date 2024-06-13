package com.test.pismo.service;

import com.test.pismo.PismoApplication;
import com.test.pismo.config.H2TestProfileConfig;
import com.test.pismo.controller.request.CreateAccountRequest;
import com.test.pismo.controller.response.AccountResponse;
import com.test.pismo.exception.DuplicatedException;
import com.test.pismo.exception.NotFoundException;
import com.test.pismo.mapper.AccountMapper;
import com.test.pismo.model.Account;
import com.test.pismo.repository.AccountRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(classes = {PismoApplication.class, H2TestProfileConfig.class})
@ActiveProfiles("test")
public class AccountServiceTests {

    @InjectMocks
    private AccountService accountService;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private AccountMapper accountMapper;

    @Test
    public void createNewAccount() {
        var createAccountRequest = new CreateAccountRequest(123456789L);

        var account = Account.builder().documentNumber(123456789L).id(1L).build();

        var response = AccountResponse.builder().documentNumber(123456789L).id(1L).build();

        Mockito.when(accountMapper.toModel(createAccountRequest)).thenReturn(account);
        Mockito.when(accountRepository.save(account)).thenReturn(account);
        Mockito.when(accountMapper.toResponse(account)).thenReturn(response);

        var newAccount = accountService.createAccount(createAccountRequest);

        Assertions.assertEquals(newAccount.getDocumentNumber(), account.getDocumentNumber());

    }

    @Test
    public void createNewAccountWithDuplicatedDocumentShouldThrowError() {
        var createAccountRequest = new CreateAccountRequest(123456789L);

        var account = Account.builder().documentNumber(123456789L).id(1L).build();

        Mockito.when(accountRepository.findByDocumentNumber(123456789L)).thenReturn(Optional.of(account));

        Mockito.when(accountMapper.toModel(createAccountRequest)).thenReturn(account);
        Mockito.when(accountRepository.save(account)).thenReturn(account);

        DuplicatedException accountException = assertThrows(DuplicatedException.class,
                () -> accountService.createAccount(createAccountRequest));


        assertEquals("Account already exists", accountException.getMessage());

    }

    @Test
    public void getAccountById() {

        var account = Account.builder().documentNumber(123456789L).id(1L).build();

        var response = AccountResponse.builder().documentNumber(123456789L).id(1L).build();

        Mockito.when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        Mockito.when(accountMapper.toResponse(account)).thenReturn(response);

        var accountResponse = accountService.getAccountById(1L);

        Assertions.assertEquals(accountResponse.getDocumentNumber(), account.getDocumentNumber());

    }

    @Test
    public void getAccountByIdShouldThrowNotFound() {

        NotFoundException notFoundException = assertThrows(NotFoundException.class,
                () -> accountService.getAccountById(1L));


        assertEquals("Account not found", notFoundException.getMessage());

    }
}
