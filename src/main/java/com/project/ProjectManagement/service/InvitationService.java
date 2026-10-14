package com.project.ProjectManagement.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.ProjectManagement.model.Invitation;
import com.project.ProjectManagement.repository.InvitationRepo;

@Service
public class InvitationService {
    @Autowired
    private InvitationRepo invitationRepo;

    @Autowired
    private EmailService emailService;  

    public void sendInvitation(String email, Long projectId) throws Exception {
        String invitationToken = UUID.randomUUID().toString();

        Invitation invitation = new Invitation();
        invitation.setEmail(email);
        invitation.setProject_id(projectId);
        invitation.setToken(invitationToken);

        invitationRepo.save(invitation);

        String invitationLink = "http://localhost:5173/accept_invitation?token="+invitationToken;
        emailService.sendEmailWithToken(email, invitationLink);
    }

    public Invitation acceptInvitation(String token, Long userId) throws Exception {
        Invitation invitation = invitationRepo.findByToken(token);
        if (invitation == null) throw new Exception("Invalid invitation token");
        return invitation;
    }

    public String getTokenByUserMail(String email) {
        Invitation invitation = invitationRepo.findByEmail(email);
        return invitation.getToken();
    }

    public void deleteToken(String token) {
        invitationRepo.deleteByToken(token);
    }
}
