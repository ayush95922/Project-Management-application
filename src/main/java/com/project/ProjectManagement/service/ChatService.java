package com.project.ProjectManagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.ProjectManagement.model.Chat;
import com.project.ProjectManagement.repository.ChatRepo;

@Service
public class ChatService {
    @Autowired
    ChatRepo repo;
    Chat createChat(Chat chat) {
        return repo.save(chat);
    }
}
