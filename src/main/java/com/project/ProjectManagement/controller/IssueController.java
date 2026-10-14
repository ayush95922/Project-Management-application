package com.project.ProjectManagement.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.ProjectManagement.model.Issue;
import com.project.ProjectManagement.model.IssueDTO;
import com.project.ProjectManagement.model.User;
import com.project.ProjectManagement.request.IssueRequest;
import com.project.ProjectManagement.response.MessageResponse;
import com.project.ProjectManagement.service.IssueService;
import com.project.ProjectManagement.service.UserService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.PutMapping;




@RestController
@RequestMapping("/api/issues")
public class IssueController {
    @Autowired
    IssueService issueService;

    @Autowired
    UserService userService;

    @GetMapping("/{issueId}")
    public ResponseEntity<Issue> getIssueById(@PathVariable Long issueId) throws Exception {
        Issue issue = issueService.getIssueById(issueId);
        return new ResponseEntity<Issue>(issue, HttpStatus.OK);
    }
    
    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<Issue>> getIssueByProjectId(@PathVariable Long projectId) throws Exception {
        return new ResponseEntity<List<Issue>>(issueService.getIssueByProjectId(projectId), HttpStatus.OK);
    }
    
    @PostMapping()
    public ResponseEntity<IssueDTO> createIssue(@RequestBody IssueRequest request, @RequestHeader("Authorization") String token) throws Exception {
        User user = userService.findUserProfileByJWT(token);
        
        if (user != null)
        {
            Issue createdIssue = issueService.creatIssue(request, user);
            IssueDTO issueDTO = new IssueDTO();
            issueDTO.setAssignee(user);
            issueDTO.setDescription(createdIssue.getDescription());
            issueDTO.setDueDate(createdIssue.getDueDate());
            issueDTO.setId(createdIssue.getId());
            issueDTO.setPriority(createdIssue.getPriority());
            issueDTO.setProjectId(createdIssue.getProject().getId());
            issueDTO.setStatus(createdIssue.getStatus());
            issueDTO.setTags(createdIssue.getTags());
            issueDTO.setTitle(createdIssue.getTitle());

            return new ResponseEntity<>(issueDTO, HttpStatus.OK);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
    
    @DeleteMapping("/{issueId}")
    public ResponseEntity<MessageResponse> deleteIssue(@PathVariable Long issueId, @RequestHeader("Authorization") String token) throws Exception
    {
        User user = userService.findUserProfileByJWT(token);
        issueService.deleteIssue(issueId, user.getId());

        return new ResponseEntity<>(new MessageResponse("Issue deleted"), HttpStatus.OK);
    }

    @PutMapping("/{issueId}/assignee/{userId}")
    public ResponseEntity<Issue> addUserToIssue(@PathVariable Long issueId, @PathVariable Long userId) throws Exception
    {
        Issue issue = issueService.addUserToIssue(issueId, userId);
        return new ResponseEntity<>(issue, HttpStatus.OK);
    }

    @PutMapping("/{issueId}/status/{status}")
    public ResponseEntity<Issue> updateIssueStatus(@PathVariable Long issueId, @PathVariable String status) throws Exception
    {
        return new ResponseEntity<>(issueService.updateStatus(issueId, status), HttpStatus.OK);
    }
}
