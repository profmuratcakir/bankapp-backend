package com.bank.payload.request;

import lombok.Data;

@Data
public class TransferRequest {
    private String recipientName;
    private Double amount;
}
