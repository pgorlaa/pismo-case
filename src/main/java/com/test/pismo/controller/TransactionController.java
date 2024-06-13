package com.test.pismo.controller;

import com.test.pismo.controller.request.CreateTransactionRequest;
import com.test.pismo.controller.response.TransactionResponse;
import com.test.pismo.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    @Autowired
    public TransactionController(final TransactionService transactionService) {
        this.transactionService = transactionService;
    }


    @PostMapping
    public ResponseEntity<String> create(@Valid @RequestBody CreateTransactionRequest createTransactionRequest) {

        transactionService.createTransaction(createTransactionRequest);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body("test");
    }

    @GetMapping("/{transactionId}")
    public ResponseEntity<TransactionResponse> getTransactionById(@PathVariable Long transactionId) {
        return ResponseEntity.ok(transactionService.getTransactionById(transactionId));
    }

}