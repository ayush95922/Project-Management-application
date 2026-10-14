package com.project.ProjectManagement.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.ProjectManagement.model.Comment;
import com.project.ProjectManagement.model.User;
import com.project.ProjectManagement.request.CommentRequest;
import com.project.ProjectManagement.response.MessageResponse;
import com.project.ProjectManagement.service.CommentService;
import com.project.ProjectManagement.service.UserService;

@RestController
@RequestMapping("/api/comments")
public class CommentController {
    @Autowired
    private CommentService commentService;

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<Comment> createComment(@RequestBody CommentRequest request, @RequestHeader("Authorization") String jwt) throws Exception
    {
        User user = userService.findUserProfileByJWT(jwt);
        return new ResponseEntity<>(commentService.createComment(request.getIssueId(), user.getId(), request.getContent()), HttpStatus.OK);
    } 

    @DeleteMapping("/{commentId}")
    public ResponseEntity<MessageResponse> deleteComment(@PathVariable Long commentId, @RequestHeader("Authorization") String jwt) throws Exception
    {
        User user = userService.findUserProfileByJWT(jwt);
        commentService.deleteComment(commentId, user.getId());
        return new ResponseEntity<>(new MessageResponse("Comment deleted"), HttpStatus.OK);
    }

    @GetMapping("/{issueId}")
    public ResponseEntity<List<Comment>> getCommentsByIssueId(@PathVariable Long issueId) throws Exception
    {
        return new ResponseEntity<>(commentService.findCommentsByIssueId(issueId), HttpStatus.OK);
    }
}
