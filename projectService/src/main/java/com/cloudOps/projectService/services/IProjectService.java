package com.cloudOps.projectService.services;

import com.cloudOps.projectService.dto.CreateProjectRequest;
import com.cloudOps.projectService.dto.ProjectResponse;

public interface IProjectService {
    ProjectResponse createProject(CreateProjectRequest request, String createdBy);

    ProjectResponse getProjectById(java.util.UUID projectId);

}
