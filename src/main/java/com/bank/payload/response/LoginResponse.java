package com.bank.payload.response;

import com.bank.dao.UserDAO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LoginResponse {
    private UserDAO user;
    private String jwt;
}
