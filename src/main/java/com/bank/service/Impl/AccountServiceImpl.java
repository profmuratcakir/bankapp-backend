package com.bank.service.Impl;

import com.bank.model.Account;
import com.bank.model.Recipient;
import com.bank.model.Transaction;
import com.bank.model.User;
import com.bank.payload.request.TransactionRequest;
import com.bank.payload.request.TransferRequest;
import com.bank.repository.AccountRepo;
import com.bank.repository.RecipientRepo;
import com.bank.service.AccountService;
import com.bank.service.TransactionService;
import com.bank.util.TransactionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    AccountRepo accountRepo;

    @Autowired
    RecipientRepo recipientRepo;

    @Autowired
    TransactionService transactionService;

    @Override
    public Account createAccount() {
        Account account = new Account();
        account.setAccountNumber(getAccountNumber());
        account.setAccountBalance(new BigDecimal(0.0));
        accountRepo.save(account);
        return accountRepo.findByAccountNumber(account.getAccountNumber());
    }

   private Long getAccountNumber(){
        long smallest = 1000_0000_0000_0000L;
        long biggest = 9999_9999_9999_9999L;
        long random = ThreadLocalRandom.current().nextLong(smallest, biggest);
        return random;
   }

    @Override
    public void deposit(TransactionRequest request, User user) {
        Account account = user.getAccount();
        Double amount = request.getAmount();
        account.setAccountBalance(account.getAccountBalance().add(new BigDecimal(amount)));
        Date date = new Date();
        Transaction transaction = new Transaction(date, request.getComment(),
                                            TransactionType.DEPOSIT.toString(),
                                            amount,account.getAccountBalance(),
                  false, account );
        List<Transaction> transactions = account.getTransactions();
        transactions.add(transaction);
        account.setTransactions(transactions);
        transactionService.saveTransaction(transaction);
        accountRepo.save(account);
    }

    @Override
    public void withdraw(TransactionRequest request, User user) {
        Account account = user.getAccount();
        Double amount = request.getAmount();
        account.setAccountBalance(account.getAccountBalance().subtract(new BigDecimal(amount)));
        Date date = new Date();
        Transaction transaction = new Transaction(date, request.getComment(),
                TransactionType.WITHDRAW.toString(),
                amount,account.getAccountBalance(),
                false, account );
        List<Transaction> transactions = account.getTransactions();
        transactions.add(transaction);
        account.setTransactions(transactions);
        transactionService.saveTransaction(transaction);
        accountRepo.save(account);
    }

    @Override
    public void saveRecipient(Recipient recipient) {
        recipientRepo.save(recipient);
    }

    @Override
    public void transfer(TransferRequest request, User user) {
        Account account = user.getAccount();
        Double amount = request.getAmount();
        account.setAccountBalance(account.getAccountBalance().subtract(new BigDecimal(amount)));

        Date date = new Date();
        Transaction transaction = new Transaction(date, request.getRecipientName(),
                TransactionType.TRANSFER.toString(),
                amount,account.getAccountBalance(),
                false, account );
        List<Transaction> transactions = account.getTransactions();
        transactions.add(transaction);
        account.setTransactions(transactions);
        accountRepo.save(account);
        transactionService.saveTransaction(transaction);
    }
}
