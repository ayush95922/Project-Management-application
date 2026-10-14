package com.project.ProjectManagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.ProjectManagement.model.User;

public interface UserRepo extends JpaRepository<User, Long>{
    User findByEmail(String email);
}
