package com.cloudOps.projectService.services;

import com.cloudOps.projectService.dto.ProjectDTO;
import com.cloudOps.projectService.dto.ProjectResponse;

import java.util.List;
import java.util.UUID;

public interface IProjectService {
    ProjectResponse createProject(ProjectDTO request, String createdBy);

    ProjectResponse getProjectById(UUID projectId);

    List<ProjectResponse> getProjectsByOrganization(UUID organizationId);

    ProjectResponse updateProject(UUID projectId, ProjectDTO request);
}
