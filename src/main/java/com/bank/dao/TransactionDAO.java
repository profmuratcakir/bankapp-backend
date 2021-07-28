package com.bank.dao;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class TransactionDAO {

    private Long id;
    private String date;
    private String time;
    private String description;
    private String type;
    private double amount;
    private BigDecimal availableBalance;
    private Boolean isTransfer;

}
