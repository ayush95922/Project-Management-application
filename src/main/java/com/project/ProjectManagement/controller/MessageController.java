package com.project.ProjectManagement.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.ProjectManagement.model.Message;
import com.project.ProjectManagement.request.ChatMessageRequest;
import com.project.ProjectManagement.service.MessageService;
import com.project.ProjectManagement.service.ProjectService;
import com.project.ProjectManagement.service.UserService;

@RestController
@RequestMapping("/api/messages")
public class MessageController {
    @Autowired
    ProjectService projectService;

    @Autowired
    UserService userService;

    @Autowired
    MessageService messageService;

    @PostMapping("/send")
    public ResponseEntity<Message> sendMessage(@RequestBody ChatMessageRequest request) throws Exception
    {
        Message sentMessage = messageService.sendMessage(request.getSenderId(), request.getProjectId(), request.getContent());
        return new ResponseEntity<>(sentMessage, HttpStatus.OK);
    }

    @GetMapping("/chat/{projectId}")
    public ResponseEntity<List<Message>> getMessagesByChatId(@PathVariable Long projectId) throws Exception
    {
        return new ResponseEntity<>(messageService.getMessagesByProjectId(projectId), HttpStatus.OK);
    }
}
