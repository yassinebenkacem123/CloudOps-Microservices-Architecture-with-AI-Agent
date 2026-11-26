package com.cloudOps.projectService.services.impl;

import com.cloudOps.projectService.dto.CreateProjectRequest;
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

@Service
@RequiredArgsConstructor

public class ProjectServiceImpl implements IProjectService {

    private final ProjectRepo projectRepo;
    private final ProjectMapper projectMapper;

    @Override
    @Transactional
    public ProjectResponse createProject(CreateProjectRequest request, String createdBy) {
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
}
