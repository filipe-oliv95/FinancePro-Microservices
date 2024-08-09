package com.financepro.accounts.service.impl;

import com.financepro.accounts.constants.AccountsConstants;
import com.financepro.accounts.dto.AccountsDto;
import com.financepro.accounts.dto.CustomerDto;
import com.financepro.accounts.entity.Accounts;
import com.financepro.accounts.entity.Customer;
import com.financepro.accounts.exception.CustomerAlreadyExistsException;
import com.financepro.accounts.exception.ResourceNotFoundException;
import com.financepro.accounts.mapper.AccountsMapper;
import com.financepro.accounts.mapper.CustomerMapper;
import com.financepro.accounts.repository.CustomerRepository;
import com.financepro.accounts.repository.AccountsRepository;
import com.financepro.accounts.service.IAccountsService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
@AllArgsConstructor
public class AccountsServiceImpl implements IAccountsService {

    private CustomerRepository customerRepository;
    private AccountsRepository accountsRepository;

    @Override
    public void createAccount(CustomerDto customerDto) {
        Customer customer = CustomerMapper.mapToCustomer(customerDto, new Customer());
        Optional<Customer> customerOptional = customerRepository.findByMobileNumber(customerDto.getMobileNumber());
        if (customerOptional.isPresent()) {
            throw new CustomerAlreadyExistsException("Customer already registered with given mobileNumber: " + customerDto.getMobileNumber());
        }
        customer.setCreatedAt(LocalDateTime.now());
        customer.setCreatedBy("Someone");

        Customer savedCustomer = customerRepository.save(customer);

        accountsRepository.save(createNewAccount(savedCustomer));
    }

    private Accounts createNewAccount(Customer savedCustomer) {
        Accounts newAccount = new Accounts();
        newAccount.setCustomerId(savedCustomer.getCustomerId());
        long randomAccNumber = 1000000000L + new Random().nextInt(900000000);
        newAccount.setAccountNumber(randomAccNumber);
        newAccount.setAccountType(AccountsConstants.SAVINGS);
        newAccount.setBranchAddress(AccountsConstants.ADDRESS);

        newAccount.setCreatedAt(LocalDateTime.now());
        newAccount.setCreatedBy("Someone");

        return newAccount;
    }

    @Override
    public CustomerDto fetchAccount(String mobileNumber) {
        Customer customer = customerRepository.findByMobileNumber(mobileNumber)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber)
                );
        Accounts accounts = accountsRepository.findByCustomerId(customer.getCustomerId())
                .orElseThrow(
                        () -> new ResourceNotFoundException("Account", "customerId", customer.getCustomerId().toString())
                );
        CustomerDto customerDto = CustomerMapper.mapToCustomerDto(customer, new CustomerDto());
        customerDto.setAccountsDto(AccountsMapper.mapToAccountsDto(accounts, new AccountsDto()));
        return customerDto;
    }

    @Override
    public boolean updateAccount(CustomerDto customerDto) {
        boolean isUpdated = false;
        AccountsDto accountsDto = customerDto.getAccountsDto();
        if(accountsDto != null ) {
            Long accountNumber = customerDto.getAccountsDto().getAccountNumber();
            Accounts accounts = accountsRepository.findById(accountNumber)
                    .orElseThrow(
                            () -> new ResourceNotFoundException("Account", "accountNumber", accountNumber.toString())
                    );
            AccountsMapper.mapToAccounts(accountsDto, accounts);
            accounts = accountsRepository.save(accounts);

            Long customerId = accounts.getCustomerId();
            Customer customer = customerRepository.findById(customerId)
                    .orElseThrow(
                            () -> new ResourceNotFoundException("Customer", "customerId", customerId.toString())
                    );
            CustomerMapper.mapToCustomer(customerDto, customer);
            isUpdated = true;
        }
        return isUpdated;
    }

    @Override
    public boolean deleteAccount(String mobileNumber) {
        return false;
    }
}
