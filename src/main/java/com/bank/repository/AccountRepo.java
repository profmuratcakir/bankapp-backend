package com.bank.repository;

import com.bank.model.Account;
import org.springframework.data.repository.CrudRepository;

public interface AccountRepo extends CrudRepository<Account, Long> {
    Account findByAccountNumber(Long accountNumber);
}
