package com.project.ProjectManagement.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.ProjectManagement.model.Chat;
import com.project.ProjectManagement.model.Invitation;
import com.project.ProjectManagement.model.Project;
import com.project.ProjectManagement.model.User;
import com.project.ProjectManagement.request.InvitationRequest;
import com.project.ProjectManagement.response.MessageResponse;
import com.project.ProjectManagement.service.InvitationService;
import com.project.ProjectManagement.service.ProjectService;
import com.project.ProjectManagement.service.UserService;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {
    @Autowired
    private ProjectService projectService;
    @Autowired
    private UserService userService;
    @Autowired
    private InvitationService invitationService;

    @GetMapping
    public ResponseEntity<List<Project>> getProjects(
        @RequestParam(required = false) String category, 
        @RequestParam(required = false) String tag,
        @RequestHeader("Authorization") String jwt) throws Exception {
            System.out.println("jwt controller: " + jwt);
            User user = userService.findUserProfileByJWT(jwt);
            List<Project> projects = projectService.getProjectByTeam(user, category, tag);
            return new ResponseEntity<>(projects, HttpStatus.OK);
        }
    
        @GetMapping("/{projectId}")
        public ResponseEntity<Project> getProjectById(
            @PathVariable Long projectId,
            @RequestHeader("Authorization") String jwt) throws Exception {
                Project project = projectService.getProjectById(projectId);
                return new ResponseEntity<>(project, HttpStatus.OK);
            }
        @PostMapping
        public ResponseEntity<Project> createProject(
            @RequestBody Project project,
            @RequestHeader("Authorization") String jwt
            ) throws Exception {
                User user = userService.findUserProfileByJWT(jwt);
                Project createdProject = projectService.createProject(project, user);
                return new ResponseEntity<>(createdProject, HttpStatus.CREATED);
            }

        @PatchMapping("/{projectId}")
        public ResponseEntity<Project> updateProject(
            @RequestBody Project project,
            @PathVariable Long projectId,
            @RequestHeader("Authorization") String jwt
            ) throws Exception {
                Project updatedProject = projectService.updateProject(project, projectId);
                return new ResponseEntity<>(updatedProject, HttpStatus.CREATED);
            }
        
        
        @DeleteMapping("/{projectId}")
        public ResponseEntity<MessageResponse> deleteProject(@PathVariable Long projectId, @RequestHeader("Authorization") String jwt) throws Exception{
            User user = userService.findUserProfileByJWT(jwt);
            projectService.deleteProject(projectId, user);
            return new ResponseEntity<>(new MessageResponse("Project deleted successfully"), HttpStatus.OK);    
        } 

        @GetMapping("/search")
        public ResponseEntity<List<Project>> searchProject(
            @RequestParam(required = false) String keyword,
            @RequestHeader("Authorization") String jwt) throws Exception {
                User user = userService.findUserProfileByJWT(jwt);
                return new ResponseEntity<>(projectService.searchProjects(keyword, user), HttpStatus.OK);
            }
        
        @GetMapping("/chat/{projectId}")
        public ResponseEntity<Chat> getProjectChatById(
            @PathVariable Long projectId) throws Exception {
                Chat chat = projectService.getChatByProject(projectId);
                return new ResponseEntity<>(chat, HttpStatus.OK);
            }
        

        @PostMapping("/invite")
        public ResponseEntity<MessageResponse> inviteProject(@RequestBody InvitationRequest invitationRequest) throws Exception {
                invitationService.sendInvitation(invitationRequest.getEmail(), invitationRequest.getProjectId());
                return new ResponseEntity<>(new MessageResponse("Invitation sent successfully"), HttpStatus.OK);
        }

        @GetMapping("/accept_invitation")
        public ResponseEntity<Invitation> acceptInviteProject(
            @RequestParam String token,
            @RequestHeader("Authorization") String jwt
            ) throws Exception {
                User user = userService.findUserProfileByJWT(jwt);
                Invitation invitation = invitationService.acceptInvitation(token, user.getId());
                projectService.addUserToProject(invitation.getProject_id(), user.getId());
                return new ResponseEntity<>(invitation, HttpStatus.OK);
        }
}
