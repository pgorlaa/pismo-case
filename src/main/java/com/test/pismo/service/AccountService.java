package com.test.pismo.service;

import com.test.pismo.controller.request.CreateAccountRequest;
import com.test.pismo.controller.response.AccountResponse;
import com.test.pismo.mapper.AccountMapper;
import com.test.pismo.repository.AccountRepository;
import com.test.pismo.exception.DuplicatedException;
import com.test.pismo.exception.NotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    private final AccountMapper accountMapper;

    @Autowired
    public AccountService(final AccountRepository accountRepository, final AccountMapper accountMapper) {
        this.accountRepository = accountRepository;
        this.accountMapper = accountMapper;
    }

    public AccountResponse getAccountById(Long accountId) {
        var account = accountRepository.findById(accountId).orElseThrow(() -> new NotFoundException("Account not found"));

        return accountMapper.toResponse(account);
    }

    @Transactional
    public AccountResponse createAccount(CreateAccountRequest createAccountRequest) {
        accountRepository.findByDocumentNumber(createAccountRequest.getDocumentNumber()).ifPresent(account -> {
            throw new DuplicatedException("Account already exists");
        });

        var account = accountMapper.toModel(createAccountRequest);

        return accountMapper.toResponse(accountRepository.save(account));
    }
}
