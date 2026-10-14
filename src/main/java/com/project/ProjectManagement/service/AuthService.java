package com.project.ProjectManagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.project.ProjectManagement.model.User;
import com.project.ProjectManagement.repository.UserRepo;
import com.project.ProjectManagement.request.LoginRequest;
import com.project.ProjectManagement.response.AuthResponse;
import com.project.ProjectManagement.security.JwtProvider;

@Service
public class AuthService {
    @Autowired
    UserRepo userRepo;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    MyUserDetailsService myUserDetailsService;

    @Autowired
    SubscriptionService subscriptionService;

    public AuthResponse createUser(User user) throws Exception
    {
        User exist = userRepo.findByEmail(user.getEmail());
        if (exist != null) throw new Exception("Email already exist with another account");

        User createdUser = new User();
        createdUser.setPassword(passwordEncoder.encode(user.getPassword()));
        createdUser.setEmail(user.getEmail());
        createdUser.setFullName(user.getFullName());

        // this constructor generates an unauthenticated(isAuthenticated set to false) Authentication object, no role here, just created when creating a new user
        Authentication authentication = new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = JwtProvider.generateToken(authentication);
        AuthResponse response = new AuthResponse();
        response.setMessage("Signup successful");
        response.setJwt(jwt);
        User savedUser = userRepo.save(createdUser); 
        subscriptionService.createSubscription(savedUser);
        return response;
    }

    public AuthResponse login(LoginRequest request)
    {
        String username = request.getEmail();
        String password = request.getPassword();
        System.out.println("password");
        Authentication authentication = authenticate(username, password);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = JwtProvider.generateToken(authentication);
        AuthResponse response = new AuthResponse();
        response.setMessage("Signin successful");
        response.setJwt(jwt);

        return response;
    }

    private Authentication authenticate(String username, String password) {
        UserDetails userDetails = myUserDetailsService.loadUserByUsername(username);
        if (userDetails == null) throw new BadCredentialsException("Invalid username");

        if (!passwordEncoder.matches(password, userDetails.getPassword())) 
            throw new BadCredentialsException("Invalid password");
        // this constructor set the isAuthenticated to true and is used when the credentials are valid and true
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }   
}
