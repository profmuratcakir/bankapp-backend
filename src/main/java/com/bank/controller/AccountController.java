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
        if(transactionRequest.getAmount() > 0) {
            accountService.deposit(transactionRequest, user);
            response.setMessage("Amount succesfully deposited");
            response.setSuccess(true);
            UserDAO userDAO = userService.getUserDAOByName(user.getUsername());
            response.setUser(userDAO);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }else{
            response.setMessage("Amount should be bigger than 0");
            response.setSuccess(false);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/withdraw")
    public ResponseEntity<TransactionResponse> withdraw (@Valid @RequestBody TransactionRequest transactionRequest){
        TransactionResponse response = new TransactionResponse();
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        // Account Balance Check
        if (user != null && user.getAccount() != null
                && user.getAccount().getAccountBalance().doubleValue() >= transactionRequest.getAmount()) {
        accountService.withdraw(transactionRequest, user);
        response.setMessage("Amount succesfully withdrawed");
        response.setSuccess(true);
        UserDAO userDAO = userService.getUserDAOByName(user.getUsername());
        response.setUser(userDAO);
        return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            response.setMessage("Sorry! you don't have sufficient amount to withdraw");
            response.setSuccess(false);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/transfer")
    public ResponseEntity<TransactionResponse> transfer (@Valid @RequestBody TransferRequest transferRequest){
        TransactionResponse response = new TransactionResponse();
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (user != null && user.getAccount() != null
                && user.getAccount().getAccountBalance().doubleValue() >= transferRequest.getAmount()) {
            accountService.transfer(transferRequest, user);
            response.setMessage("Amount transfered succesfully");
        response.setSuccess(true);
        UserDAO userDAO = userService.getUserDAOByName(user.getUsername());
        response.setUser(userDAO);
        return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            response.setMessage("Amount is not sufficient");
            response.setSuccess(false);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

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
