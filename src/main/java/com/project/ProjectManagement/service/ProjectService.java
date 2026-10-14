package com.project.ProjectManagement.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.ProjectManagement.model.Chat;
import com.project.ProjectManagement.model.Project;
import com.project.ProjectManagement.model.User;
import com.project.ProjectManagement.repository.ProjectRepo;

@Service
public class ProjectService {
    @Autowired
    private ProjectRepo projectRepo;

    @Autowired
    private UserService userService;

    @Autowired
    private ChatService chatService;
    
    public Project createProject(Project project, User user) throws Exception {
        Project createdProject = new Project();
        createdProject.setOwner(user);
        createdProject.setTags(project.getTags());
        createdProject.setName(project.getName());
        createdProject.setCategory(project.getCategory());
        createdProject.setDescription(project.getDescription());
        createdProject.getTeam().add(user);

        Project savedProject = projectRepo.save(createdProject);

        Chat chat = new Chat();
        chat.setProject(savedProject);
        chatService.createChat(chat);
        savedProject.setChat(chat);

        return savedProject;
    }

    public List<Project> getProjectByTeam(User user, String category, String tag) throws Exception {
        List<Project> projects = projectRepo.findByTeamContainingOrOwner(user, user);
        if (category != null)
            projects = projects.stream()
                                .filter(project -> project.getCategory().equals(category))
                                .collect(Collectors.toList());
        if (tag != null)
            projects = projects.stream()
                                .filter(project -> project.getTags().contains(tag))
                                .collect(Collectors.toList());        
        return projects;
    }

    public Project getProjectById(Long projectId) throws Exception {
        Optional<Project> project = projectRepo.findById(projectId);
        if (project.isEmpty()) throw new Exception("Project not found");
        return project.get();
    }

    public void deleteProject(Long projectId, User user) throws Exception {
        Project project = getProjectById(projectId);
        if (!project.getOwner().equals(user)) throw new Exception("You are not the owner of this project");
        projectRepo.deleteById(projectId);
    }

    public Project updateProject(Project updatedProject, Long id) throws Exception {
        Project project = getProjectById(id);

        project.setCategory(updatedProject.getCategory());
        project.setDescription(updatedProject.getDescription());
        project.setName(updatedProject.getName());
        project.setTags(updatedProject.getTags());

        return projectRepo.save(project);
    }

    public void addUserToProject(Long projectId, Long userId) throws Exception {
        Project project = getProjectById(projectId);
        User user = userService.findUserById(userId);
        if (!project.getTeam().contains(user))
        {
            project.getChat().getUsers().add(user);
            project.getTeam().add(user);
        }
        projectRepo.save(project);
    }

    public void removeUserFromProject(Long projectId, Long userId) throws Exception {
        Project project = getProjectById(projectId);
        User user = userService.findUserById(userId);
        if (project.getTeam().contains(user))
        {
            project.getChat().getUsers().remove(user);
            project.getTeam().remove(user);
        }
        projectRepo.save(project);
    }

    public Chat getChatByProject(Long projectId) throws Exception {
        Project project = getProjectById(projectId);
        return project.getChat();
    }

    public List<Project> searchProjects(String keyword, User user) throws Exception {
        return projectRepo.findByNameContainingAndTeamContaining(keyword, user);
    }
}
