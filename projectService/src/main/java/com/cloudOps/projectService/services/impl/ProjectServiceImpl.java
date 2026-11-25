package com.cloudOps.projectService.services.impl;

import com.cloudOps.projectService.dto.CreateProjectRequest;
import com.cloudOps.projectService.dto.ProjectResponse;
import com.cloudOps.projectService.exception.ProjectAlreadyExistsException;
import com.cloudOps.projectService.mappers.ProjectMapper;
import com.cloudOps.projectService.models.Project;
import com.cloudOps.projectService.repository.ProjectRepo;
import com.cloudOps.projectService.services.IProjectService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProjectServiceImpl implements IProjectService {

    private final ProjectRepo projectRepo;
    private final ProjectMapper projectMapper;

    @Override
    @Transactional
    public ProjectResponse createProject(CreateProjectRequest request, String createdBy) {
        log.info("Creating project with name: {} for organization: {}", request.getName(), request.getOrganizationId());

        if (projectRepo.existsByOrganizationIdAndSlug(request.getOrganizationId(), request.getSlug())) {
            log.error("Project with slug: {} already exists for organization: {}", request.getSlug(), request.getOrganizationId());
            throw new ProjectAlreadyExistsException("Project already exists with the given slug for this organization");
        }

        Project project = projectMapper.toEntity(request);
        // Note: createdBy could be used to set an audit field if available in Project model
        // For now, it's passed as a parameter but not stored in Project entity based on current fields.

        Project savedProject = projectRepo.save(project);
        log.info("Project created successfully with ID: {}", savedProject.getProjectId());

        ProjectResponse response = projectMapper.toResponse(savedProject);
        response.setCreatedBy(createdBy);

        return response;
    }
}
