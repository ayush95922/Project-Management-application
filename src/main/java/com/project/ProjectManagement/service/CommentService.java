package com.project.ProjectManagement.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.ProjectManagement.model.Comment;
import com.project.ProjectManagement.model.Issue;
import com.project.ProjectManagement.model.User;
import com.project.ProjectManagement.repository.CommentRepo;

@Service
public class CommentService {
    @Autowired
    IssueService issueService;

    @Autowired
    UserService userService;

    @Autowired
    CommentRepo commentRepo;
    public Comment createComment(Long issueId, Long userId, String comment) throws Exception
    {
        Issue issue = issueService.getIssueById(issueId);
        Comment createdComment = new Comment();
        createdComment.setContent(comment);
        createdComment.setIssue(issue);    
        createdComment.setUser(userService.findUserById(userId));

        createdComment.setCreatedAt(LocalDateTime.now());

        Comment savedComment = commentRepo.save(createdComment);
        issue.getComments().add(savedComment);
        return savedComment;
    }


    public void deleteComment(Long commentId, Long userId) throws Exception
    {
        User user = userService.findUserById(userId);
        Optional<Comment> commentOptional = commentRepo.findById(commentId);

        if (commentOptional.isEmpty())
        {
            throw new Exception("Comment not found with id " + commentId);
        }

        Comment comment = commentOptional.get();
        if (comment.getUser().equals(user))
            commentRepo.delete(comment);
        else throw new Exception("User does not have permission to delete this comment");
    }


    public List<Comment> findCommentsByIssueId(Long issueId)
    {
        return commentRepo.findByIssueId(issueId);
    }
}
