package com.cloudOps.projectService.services.impl;

import com.cloudOps.projectService.dto.ProjectDTO;
import com.cloudOps.projectService.dto.ProjectResponse;
import com.cloudOps.projectService.exception.ProjectAlreadyExistsException;
import com.cloudOps.projectService.exception.ProjectNotFoundException;
import com.cloudOps.projectService.mappers.ProjectMapper;
import com.cloudOps.projectService.models.Project;
import com.cloudOps.projectService.repository.ProjectRepo;
import com.cloudOps.projectService.services.IProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor

public class ProjectServiceImpl implements IProjectService {

    private final ProjectRepo projectRepo;
    private final ProjectMapper projectMapper;

    @Override
    @Transactional
    public ProjectResponse createProject(ProjectDTO request, String createdBy) {
        if (projectRepo.existsByOrganizationIdAndSlug(request.getOrganizationId(), request.getSlug())) {
            throw new ProjectAlreadyExistsException(
                    "Project with slug " + request.getSlug() + " already exists in organization " + request.getOrganizationId()
            );
        }

        Project project = projectMapper.toEntity(request);
        Project savedProject = projectRepo.save(project);
        ProjectResponse response = projectMapper.toResponse(savedProject);
        response.setCreatedBy(createdBy);
        
        return response;
    }

    @Override
    public ProjectResponse getProjectById(java.util.UUID projectId) {
        Project project = projectRepo.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(
                        "Project", "id", projectId.toString()
                ));
        return projectMapper.toResponse(project);
    }

    @Override
    public List<ProjectResponse> getProjectsByOrganization(UUID organizationId) {
        return projectRepo.findAllByOrganizationId(organizationId).stream()
                .map(projectMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public ProjectResponse updateProject(UUID projectId, ProjectDTO request) {
        Project existingProject = projectRepo.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException("Project", "id", projectId.toString()));

        // Check if slug is being changed and if new slug already exists in organization
        if (!existingProject.getSlug().equals(request.getSlug()) || !existingProject.getOrganizationId().equals(request.getOrganizationId())) {
            if (projectRepo.existsByOrganizationIdAndSlug(request.getOrganizationId(), request.getSlug())) {
                throw new ProjectAlreadyExistsException(
                        "Project with slug " + request.getSlug() + " already exists in organization " + request.getOrganizationId()
                );
            }
        }

        projectMapper.updateEntityFromDto(request, existingProject);
        Project updatedProject = projectRepo.save(existingProject);
        return projectMapper.toResponse(updatedProject);
    }
}
