package com.project.ProjectManagement.service;

public interface EmailService {
    void sendEmailWithToken(String email, String link) throws Exception;
}
