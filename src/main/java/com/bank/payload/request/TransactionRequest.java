package com.bank.payload.request;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class TransactionRequest {

    @NotNull(message = "Amount can not be blank")
    private Double amount;

    @NotBlank
    private String comment;

}
