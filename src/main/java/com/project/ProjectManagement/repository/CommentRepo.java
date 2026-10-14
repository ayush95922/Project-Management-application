package com.project.ProjectManagement.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.ProjectManagement.model.Comment;

public interface CommentRepo extends JpaRepository<Comment, Long> {
    List<Comment> findByIssueId(Long issueId);
}
