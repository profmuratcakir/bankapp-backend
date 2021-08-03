package com.bank.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
public class Recipient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    private String phone;
    private String bankName;
    private String bankNumber;

    public Recipient(String name, String email, String phone,
                     String bankName, String bankNumber) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.bankName = bankName;
        this.bankNumber = bankNumber;
    }

    @ManyToOne
    @JoinColumn(name="user_id")
    @JsonIgnore
    private User user;
}
