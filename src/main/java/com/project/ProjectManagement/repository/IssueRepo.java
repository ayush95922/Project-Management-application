package com.project.ProjectManagement.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.ProjectManagement.model.Issue;

public interface IssueRepo extends JpaRepository<Issue, Long> {
    public List<Issue> findByProjectId(Long projectId);
}
