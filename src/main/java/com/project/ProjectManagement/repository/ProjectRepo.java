package com.project.ProjectManagement.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
// import org.springframework.data.jpa.repository.Query;
// import org.springframework.data.repository.query.Param;

import com.project.ProjectManagement.model.Project;
import com.project.ProjectManagement.model.User;

public interface ProjectRepo extends JpaRepository<Project, Long>{
    List<Project> findByOwner(User owner);

    List<Project> findByNameContainingAndTeamContaining(String partialName, User user);

    // @Query("SELECT p from Project p JOIN p.team t where t:=user")
    // List<Project> findProjectByTeam(@Param("user") User user);

    List<Project> findByTeamContainingOrOwner(User user, User owner);
}
