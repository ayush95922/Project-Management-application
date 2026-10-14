package com.project.ProjectManagement.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.project.ProjectManagement.model.Subscription;

public interface SubscriptionRepo extends JpaRepository<Subscription, Long> {
    public Subscription findByUserId(Long userId);
}
