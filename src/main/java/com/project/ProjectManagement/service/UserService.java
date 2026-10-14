package com.project.ProjectManagement.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.ProjectManagement.model.User;
import com.project.ProjectManagement.repository.UserRepo;
import com.project.ProjectManagement.security.JwtProvider;

@Service
public class UserService {
    @Autowired
    public UserRepo repo;

    public User findUserProfileByJWT(String jwt) throws Exception {
        String email = JwtProvider.getEmailFromJwtToken(jwt);
        return findUserByEmail(email);
    }   

    public User findUserByEmail(String email) throws Exception {
        User user = repo.findByEmail(email);
        if (user == null) throw new Exception("User not found");
        return user;
    }

    public User findUserById(Long id) throws Exception {
        Optional<User> user = repo.findById(id);
        if (user.isEmpty()) throw new Exception("User not found");
        return user.get();
    }

    public User updateUserProjectSize(User user, int size) {
        user.setProjectSize(user.getProjectSize() + size);
        return user;
    }
}
