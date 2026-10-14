package com.project.ProjectManagement.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.ProjectManagement.model.Chat;
import com.project.ProjectManagement.model.Message;
import com.project.ProjectManagement.model.User;
import com.project.ProjectManagement.repository.MessageRepo;

@Service
public class MessageService {
    @Autowired
    MessageRepo messageRepo;

    @Autowired
    UserService userService;

    @Autowired
    ProjectService projectService;

    public Message sendMessage(Long senderId, Long projectId, String content) throws Exception
    {
        User user = userService.findUserById(senderId);
        Chat chat = projectService.getChatByProject(projectId);

        Message message = new Message();
        message.setChat(chat);
        message.setContent(content);
        message.setCreatedAt(LocalDateTime.now());
        message.setSender(user);

        Message savedMessage = messageRepo.save(message);
        chat.getMessages().add(savedMessage);
        return savedMessage;
    }

    public List<Message> getMessagesByProjectId(Long projectId) throws Exception
    {
       Chat chat = projectService.getChatByProject(projectId);
       return messageRepo.findByChatIdOrderByCreatedAt(chat.getId());
    }
}
