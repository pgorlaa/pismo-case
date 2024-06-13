package com.test.pismo.controller;

import com.test.pismo.controller.request.CreateAccountRequest;
import com.test.pismo.controller.response.AccountResponse;
import com.test.pismo.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final AccountService accountService;

    @Autowired
    public AccountController(final AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/{accountId}")
    public ResponseEntity<AccountResponse> getByAccountId(@PathVariable Long accountId) {

        return ResponseEntity
                .ok()
                .body(accountService.getAccountById(accountId));
    }

    @PostMapping
    public ResponseEntity<AccountResponse> create(@Valid @RequestBody CreateAccountRequest createUserRequest) {

        var newAccount = accountService.createAccount(createUserRequest);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(newAccount);
    }

}