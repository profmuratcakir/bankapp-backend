package com.bank.dao;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class TransactionDAO {
    private Long id;
    private Date date;
    private String description;
    private String type;
    private double amount;
    private BigDecimal availableBalance;
    private Boolean isTransfer;

}
