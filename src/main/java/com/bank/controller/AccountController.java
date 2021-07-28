package com.bank.controller;

import com.bank.dao.UserDAO;
import com.bank.model.Recipient;
import com.bank.model.User;
import com.bank.payload.request.RecipientForm;
import com.bank.payload.request.TransactionRequest;
import com.bank.payload.request.TransferRequest;
import com.bank.payload.response.TransactionResponse;
import com.bank.service.AccountService;
import com.bank.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/account")
public class AccountController {

    @Autowired
    AccountService accountService;

    @Autowired
    UserService userService;

    @PostMapping("/deposit")
    public ResponseEntity<TransactionResponse> deposit (@Valid @RequestBody TransactionRequest transactionRequest){
        TransactionResponse response = new TransactionResponse();
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        accountService.deposit(transactionRequest, user);
        response.setMessage("Amount succesfully deposited");
        response.setSuccess(true);
        UserDAO userDAO = userService.getUserDAOByName(user.getUsername());
        response.setUser(userDAO);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @PostMapping("/withdraw")
    public ResponseEntity<TransactionResponse> withdraw (@Valid @RequestBody TransactionRequest transactionRequest){
        TransactionResponse response = new TransactionResponse();
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        accountService.withdraw(transactionRequest, user);
        response.setMessage("Amount succesfully withdrawed");
        response.setSuccess(true);
        UserDAO userDAO = userService.getUserDAOByName(user.getUsername());
        response.setUser(userDAO);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/transfer")
    public ResponseEntity<TransactionResponse> transfer (@Valid @RequestBody TransferRequest transferRequest){
        TransactionResponse response = new TransactionResponse();
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        accountService.transfer(transferRequest, user);
        response.setMessage("Amount succesfully transfered");
        response.setSuccess(true);
        UserDAO userDAO = userService.getUserDAOByName(user.getUsername());
        response.setUser(userDAO);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/addRecipient")
    public ResponseEntity<TransactionResponse> addRecipient (@Valid @RequestBody RecipientForm request){
        TransactionResponse response = new TransactionResponse();
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Recipient recipient = new Recipient(request.getName(), request.getEmail(),request.getPhone(),
                request.getBankName(), request.getBankNumber());
        recipient.setUser(user);
        accountService.saveRecipient(recipient);
        response.setMessage("Recipient succesfully added");
        response.setSuccess(true);
        UserDAO userDAO = userService.getUserDAOByName(user.getUsername());
        response.setUser(userDAO);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
