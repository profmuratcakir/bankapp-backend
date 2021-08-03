package com.bank.service.Impl;

import com.bank.dao.RecipientDAO;
import com.bank.dao.TransactionDAO;
import com.bank.dao.UserDAO;
import com.bank.model.Account;
import com.bank.model.Recipient;
import com.bank.model.Transaction;
import com.bank.model.User;
import com.bank.repository.AccountRepo;
import com.bank.repository.TransactionRepo;
import com.bank.repository.UserRepo;
import com.bank.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.bank.util.DateUtil.getDateAsString;
import static com.bank.util.DateUtil.SIMPLE_DATE_FORMAT;
import static com.bank.util.DateUtil.SIMPLE_DATE_TIME_FORMAT;

@Service
public class UserDetailServiceImpl implements UserDetailsService, UserService {

    @Autowired
    UserRepo userRepo;

    @Autowired
    TransactionRepo transactionRepo;

    @Autowired
    AccountRepo accountRepo;

    @Override
    public UserDAO getUserDAO(User user) {
      UserDAO userDAO = new UserDAO();
      userDAO.setUserId(user.getUserId());
      userDAO.setUsername(user.getUsername());
      userDAO.setFirstName(user.getFirstName());
      userDAO.setLastName(user.getLastName());
      userDAO.setEmail(user.getEmail());
      Boolean isAdmin = user.
              getUserRoles().
              stream().
              filter( role -> role.getRole().getName().equals("ADMIN")).findAny().isPresent();

        userDAO.setIsAdmin(isAdmin);

        if (isAdmin) {
            List<Transaction> transactions = (List<Transaction>) transactionRepo.findAll();
            List<TransactionDAO> transactionDAOs = transactions.stream()
                    .map(this::getTransactionDAO)
                    .collect(Collectors.toList());

            userDAO.setTransactions(transactionDAOs);
            userDAO.setTotalUsers(userRepo.count());
            List<Account> accounts = (List<Account>) accountRepo.findAll();
            Double totalBalance = accounts.stream().mapToDouble(account ->
                    account.getAccountBalance().doubleValue()).sum();
            userDAO.setTotalBalance(totalBalance);
        } else {

            if (user.getAccount() != null) {
                userDAO.setAccountNumber(user.getAccount().getAccountNumber());
                userDAO.setAccountBalance(user.getAccount().getAccountBalance());

                List<TransactionDAO> transactions = user.getAccount().getTransactions().stream()
                        .map(this::getTransactionDAO)
                        .collect(Collectors.toList());

                userDAO.setTransactions(transactions);

                List<RecipientDAO> recipients = user.getRecipients().stream().map(this::getRecipientDAO)
                        .collect(Collectors.toList());
                userDAO.setRecipients(recipients);
            }
        }
        return userDAO;
    }

    private TransactionDAO getTransactionDAO(Transaction transaction){
        TransactionDAO transactionDAO = new TransactionDAO();
        transactionDAO.setId(transaction.getId());
        transactionDAO.setDate(getDateAsString(transaction.getDate(),SIMPLE_DATE_FORMAT));
        transactionDAO.setTime(getDateAsString(transaction.getDate(),SIMPLE_DATE_TIME_FORMAT));
        transactionDAO.setAmount(transaction.getAmount());
        transactionDAO.setAvailableBalance(transaction.getAvailableBalance());
        transactionDAO.setDescription(transaction.getDescription());
        transactionDAO.setType(transaction.getType());
        transactionDAO.setIsTransfer(transaction.getIsTransfer());
        return transactionDAO;
    }

    private RecipientDAO getRecipientDAO(Recipient recipient){
        RecipientDAO recipientDAO = new RecipientDAO();
        recipientDAO.setId(recipient.getId());
        recipientDAO.setName(recipient.getName());
        recipientDAO.setEmail(recipient.getEmail());
        recipientDAO.setPhone(recipient.getPhone());
        recipientDAO.setBankName(recipient.getBankName());
        recipientDAO.setBankNumber(recipient.getBankNumber());
        return recipientDAO;
    }

    @Override
    public UserDAO getUserDAOByName(String username) {
        UserDAO userDAO = null;
        Optional<User> user = userRepo.findByUsername(username);
        if(user.isPresent()){
            userDAO = getUserDAO(user.get());
        }
        return userDAO;
    }

    @Override
    public List<UserDAO> getAllUsers() {
      List <User> users = (List<User>) userRepo.findAll();
      return users.stream().map(this::transformUsers).collect(Collectors.toList());

    }

    @Override
    public List<RecipientDAO> getRecipients(String username) {
       List<RecipientDAO> recipients = null;
       Optional <User> userOptional = userRepo.findByUsername(username);

       if(userOptional.isPresent()){
           User user = userOptional.get();
           recipients = user.getRecipients().stream().map(this::getRecipientDAO).
                        collect(Collectors.toList());
       }
       return recipients;
    }

    public UserDAO transformUsers(User user) {
        UserDAO userDAO = new UserDAO();
        userDAO.setUserId(user.getUserId());
        userDAO.setUsername(user.getUsername());
        userDAO.setFirstName(user.getFirstName());
        userDAO.setLastName(user.getLastName());
        userDAO.setEmail(user.getEmail());
        return userDAO;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByUsername(username).orElseThrow( () ->
                new UsernameNotFoundException("Username not found " + username));

        return user;
    }

    @Override
    public void deleteUser(Long id) {
        userRepo.deleteById(id);
    }


}