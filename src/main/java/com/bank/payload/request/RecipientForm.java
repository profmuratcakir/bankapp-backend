package com.bank.payload.request;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class RecipientForm {
    @NotBlank
    private String name;
    @Email
    private String email;

    private String phone;

    @NotBlank
    @Size(min=3, max=50)
    private String bankName;

    @NotBlank
    @Size(min=16, max=16)
    private String bankNumber;

}
