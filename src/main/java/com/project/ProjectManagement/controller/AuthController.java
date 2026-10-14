package com.project.ProjectManagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.ProjectManagement.model.User;
import com.project.ProjectManagement.request.LoginRequest;
import com.project.ProjectManagement.response.AuthResponse;
import com.project.ProjectManagement.service.AuthService;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    AuthService service;
    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> createUser(@RequestBody User user) throws Exception
    {
        return new ResponseEntity<>(service.createUser(user), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> signin(@RequestBody LoginRequest request)
    {
        return new ResponseEntity<>(service.login(request), HttpStatus.OK);
    }
}
