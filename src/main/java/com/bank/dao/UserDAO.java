package com.bank.dao;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDAO {

    private Long userId;
    private String username;

    @JsonIgnore
    private String password;

    private  String firstName;
    private  String lastName;
    private String email;
    private Boolean isAdmin;
}
