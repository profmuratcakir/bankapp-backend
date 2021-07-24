package com.bank.service;
import com.bank.dao.UserDAO;
import com.bank.model.User;

import java.util.List;

public interface UserService {
    UserDAO getUserDAO(User user);
    UserDAO getUserDAOByName(String username);
    List<UserDAO> getAllUsers();
 }
