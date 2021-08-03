package com.bank.controller;

import com.bank.dao.UserDAO;
import com.bank.payload.response.UserResponse;
import com.bank.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("/all")
    public ResponseEntity<UserResponse> getAllUsers() {
        UserResponse response = new UserResponse();
        List<UserDAO> users = userService.getAllUsers();
        response.setUsers(users);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<HttpStatus> deleteUser
            (@PathVariable(value = "id") String id) {
        userService.deleteUser(Long.parseLong(id));
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
