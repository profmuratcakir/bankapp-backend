package com.bank.controller;

import com.bank.config.jwt.JwtUtil;
import com.bank.dao.UserDAO;
import com.bank.model.Role;
import com.bank.model.User;
import com.bank.model.UserRole;
import com.bank.payload.request.LoginForm;
import com.bank.payload.request.SignUpForm;
import com.bank.payload.response.LoginResponse;
import com.bank.payload.response.Response;
import com.bank.repository.RoleRepo;
import com.bank.repository.UserRepo;

import com.bank.service.AccountService;
import com.bank.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.Set;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/auth")
public class LoginController {

    @Autowired
    UserRepo userRepo;

    @Autowired
    RoleRepo roleRepo;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    UserService userService;

    @Autowired
    AccountService accountService;

    @PostMapping("/register")
    public ResponseEntity<Response> registerUser(@Valid @RequestBody SignUpForm signUpForm){
        // Response object
        Response response = new Response();

        // Check the email and username in the signupForm
        if(userRepo.existsByUsername(signUpForm.getUsername())){
            response.setMessage("Error: Username is already taken");
            response.setSuccess(false);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        if(userRepo.existsByEmail(signUpForm.getEmail())){
            response.setMessage("Error: Email is already taken");
            response.setSuccess(false);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        //Create a new user
         User user = new User(
                 signUpForm.getUsername(),
                 encoder.encode(signUpForm.getPassword()),
                 signUpForm.getFirstName(),
                 signUpForm.getLastName(),
                 signUpForm.getEmail()
                 );

        Set<UserRole> userRoles = new HashSet<>();
        Set<String> stringRoles = signUpForm.getRole();

        stringRoles.forEach( roleName -> {
            Role role = roleRepo.findByName(roleName).
                    orElseThrow( () -> new RuntimeException("User Role Not Found"));
            userRoles.add(new UserRole(user,role));
        });

        user.setUserRoles(userRoles);
        user.setAccount(accountService.createAccount());
        userRepo.save(user);
        response.setMessage("user Registered successfuly");
        response.setSuccess(true);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginForm loginForm){

        Authentication authentication = authenticationManager.
                authenticate(new UsernamePasswordAuthenticationToken(loginForm.getUsername(),
                                                                    loginForm.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        User user =  (User) authentication.getPrincipal();

        String jwt = jwtUtil.generateToken(authentication);

        UserDAO userDAO = userService.getUserDAO(user);
        return  ResponseEntity.ok( new LoginResponse(userDAO,jwt));
    }
}
