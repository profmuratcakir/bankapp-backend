package com.bank.payload.request;
import lombok.Data;

@Data
public class TransactionRequest {

    private Double amount;
    private String comment;

}
