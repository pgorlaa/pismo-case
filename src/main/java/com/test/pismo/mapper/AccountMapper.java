package com.test.pismo.mapper;

import com.test.pismo.controller.request.CreateAccountRequest;
import com.test.pismo.controller.response.AccountResponse;
import com.test.pismo.model.Account;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AccountMapper {

    @Autowired
    private ModelMapper modelMapper;

    public AccountResponse toResponse(Account account) {
        return modelMapper.map(account, AccountResponse.class);
    }

    public Account toModel(CreateAccountRequest createAccountRequest) {
        return modelMapper.map(createAccountRequest, Account.class);
    }

}
