package com.bank.repository;

import com.bank.model.Recipient;
import org.springframework.data.repository.CrudRepository;

public interface RecipientRepo extends CrudRepository<Recipient, Long> {
}
