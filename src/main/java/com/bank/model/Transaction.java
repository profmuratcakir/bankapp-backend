package com.bank.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Date date;
    private String description;
    private String type;
    private double amount;
    private BigDecimal availableBalance;
    private Boolean isTransfer;

    @ManyToOne
    @JoinColumn(name= "account_id")
    private Account account;

    public Transaction(Date date, String description, String type, double amount,
                       BigDecimal availableBalance, Boolean isTransfer, Account account) {
        this.date = date;
        this.description = description;
        this.type = type;
        this.amount = amount;
        this.availableBalance = availableBalance;
        this.isTransfer = isTransfer;
        this.account = account;
    }
}
