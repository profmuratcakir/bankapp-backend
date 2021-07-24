package com.bank.repository;

import com.bank.model.Role;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RoleRepo extends CrudRepository<Role, Integer> {
    Optional<Role> findByName(String name);
}
