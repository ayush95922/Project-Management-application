package com.project.ProjectManagement.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.ProjectManagement.model.Issue;
import com.project.ProjectManagement.model.User;
import com.project.ProjectManagement.repository.IssueRepo;
import com.project.ProjectManagement.request.IssueRequest;

@Service
public class IssueService {
    @Autowired
    IssueRepo issueRepo;

    @Autowired
    ProjectService projectService;

    @Autowired
    UserService userService;

    public Issue getIssueById(Long id) throws Exception
    {
        Optional<Issue> issue = issueRepo.findById(id);
        if (issue.isPresent()) return issue.get();
        throw new Exception("Issue with id " + id + " not found");
    }


    public List<Issue> getIssueByProjectId(Long projectId) throws Exception
    {
        projectService.getProjectById(projectId);
        return issueRepo.findByProjectId(projectId);
    }

    
    public Issue creatIssue(IssueRequest request, User user) throws Exception
    {
        Issue issue = new Issue();
        issue.setTitle(request.getTitle());
        issue.setDescription(request.getDescription());
        issue.setProject(projectService.getProjectById(request.getProjectId()));
        issue.setStatus(request.getStatus());
        issue.setPriority(request.getPriority());
        issue.setDueDate(request.getDueDate());
        issue.setAssignee(user);

        return issueRepo.save(issue);
    }


    public void deleteIssue(Long issueId, Long userId) throws Exception
    {
        getIssueById(issueId);
        issueRepo.deleteById(issueId);
        return;
    }


    public Issue addUserToIssue(Long issueId, Long userId) throws Exception
    {
        User user = userService.findUserById(userId);
        Issue issue = getIssueById(issueId);
        issue.setAssignee(user);
        return issueRepo.save(issue);
    }

    
    public Issue updateStatus(Long issueId, String status) throws Exception
    {
        Issue issue = getIssueById(issueId);
        issue.setStatus(status);
        return issueRepo.save(issue);
    }
}
