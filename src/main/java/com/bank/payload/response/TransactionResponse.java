package com.bank.payload.response;

import com.bank.dao.UserDAO;
import lombok.Data;

@Data
public class TransactionResponse {
    boolean isSuccess;
    String message;
    UserDAO user;
}
