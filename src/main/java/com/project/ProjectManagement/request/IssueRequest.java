package com.project.ProjectManagement.request;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IssueRequest {
    private String title;
    private String description;
    private Long projectId;
    private String status;
    private String priority;
    private LocalDate dueDate;
}