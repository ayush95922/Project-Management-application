package com.project.ProjectManagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.ProjectManagement.model.Invitation;

public interface InvitationRepo extends JpaRepository<Invitation, Long> {
    Invitation findByEmail(String email);

    Invitation findByToken(String token);

    Long deleteByToken(String token);
}
