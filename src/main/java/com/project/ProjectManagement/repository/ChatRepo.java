package com.project.ProjectManagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.ProjectManagement.model.Chat;

@Repository
public interface ChatRepo  extends JpaRepository<Chat, Long>{
    
}
