package com.project.ProjectManagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.ProjectManagement.model.Message;
import java.util.List;


public interface MessageRepo extends JpaRepository<Message, Long> {
    List<Message> findByChatIdOrderByCreatedAt(Long chatId);
}
