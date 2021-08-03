package com.bank.dao;

import lombok.Data;

@Data
public class RecipientDAO {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private String bankName;
    private String bankNumber;
}
