package com.bank.dao;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDAO {
    private Long userId;
    private String username;
    @JsonIgnore
    private String password;
    private  String firstName;
    private  String lastName;
    private String email;
    private Boolean isAdmin;
    private Long accountNumber;
    private BigDecimal accountBalance;
    private List<TransactionDAO> transactions;
    private List<RecipientDAO> recipients;
    private Long totalUsers;
    private Double totalBalance;
}
