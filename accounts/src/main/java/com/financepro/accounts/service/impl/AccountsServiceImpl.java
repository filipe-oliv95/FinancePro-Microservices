package com.financepro.accounts.service.impl;

import com.financepro.accounts.dto.CustomerDto;
import com.financepro.accounts.service.IAccountsService;
import org.springframework.stereotype.Service;

@Service
public class AccountsServiceImpl implements IAccountsService {


    @Override
    public void createAccount(CustomerDto customerDto) {

    }

    @Override
    public CustomerDto fetchAccount(String mobileNumber) {
        return null;
    }

    @Override
    public boolean updateAccount(CustomerDto customerDto) {
        return false;
    }

    @Override
    public boolean deleteAccount(String mobileNumber) {
        return false;
    }
}
