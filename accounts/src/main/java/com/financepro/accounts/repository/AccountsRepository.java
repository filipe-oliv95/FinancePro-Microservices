package com.financepro.accounts.repository;

import com.financepro.accounts.entity.Accounts;
import com.financepro.accounts.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountsRepository extends JpaRepository<Accounts, Long> {
}
