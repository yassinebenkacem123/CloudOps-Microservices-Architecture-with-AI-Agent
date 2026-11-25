package com.cloudOps.projectService.mappers;

import com.cloudOps.projectService.dto.CreateProjectRequest;
import com.cloudOps.projectService.dto.ProjectResponse;
import com.cloudOps.projectService.models.Project;
import org.springframework.stereotype.Component;

@Component
public class ProjectMapper {

    public Project toEntity(CreateProjectRequest request) {
        if (request == null) {
            return null;
        }
        Project project = new Project();
        project.setName(request.getName());
        project.setSlug(request.getSlug());
        project.setDescription(request.getDescription());
        project.setOrganizationId(request.getOrganizationId());
        return project;
    }

    public ProjectResponse toResponse(Project project) {
        if (project == null) {
            return null;
        }
        ProjectResponse response = new ProjectResponse();
        response.setProjectId(project.getProjectId());
        response.setName(project.getName());
        response.setSlug(project.getSlug());
        response.setDescription(project.getDescription());
        response.setOrganizationId(project.getOrganizationId());
        response.setStatus(project.getProjectStatus());
        response.setCreatedAt(project.getCreatedAt());
        response.setUpdatedAt(project.getUpdatedAt());
        return response;
    }
}
