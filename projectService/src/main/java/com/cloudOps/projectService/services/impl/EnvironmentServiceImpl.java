package com.cloudOps.projectService.services.impl;

import com.cloudOps.projectService.constant.ProjectStatus;
import com.cloudOps.projectService.dto.EnvironmentDTO;
import com.cloudOps.projectService.dto.EnvironmentResponse;
import com.cloudOps.projectService.exception.ProjectNotFoundException;
import com.cloudOps.projectService.mappers.EnvironmentMapper;
import com.cloudOps.projectService.models.Environment;
import com.cloudOps.projectService.models.Project;
import com.cloudOps.projectService.repository.EnvironmentRepo;
import com.cloudOps.projectService.repository.ProjectRepo;
import com.cloudOps.projectService.services.IEnvironmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EnvironmentServiceImpl implements IEnvironmentService {

    private final EnvironmentRepo environmentRepo;
    private final ProjectRepo projectRepo;
    private final EnvironmentMapper environmentMapper;

    @Override
    @Transactional
    public EnvironmentResponse createEnvironment(UUID projectId, EnvironmentDTO request) {
        Project project = getActiveProject(projectId);

        if (environmentRepo.existsByProjectProjectIdAndName(projectId, request.getName())) {
            throw new RuntimeException("Environment with name " + request.getName() + " already exists in project " + projectId);
        }

        Environment environment = environmentMapper.toEntity(request);
        environment.setProject(project);
        return environmentMapper.toResponse(environmentRepo.save(environment));
    }

    @Override
    public List<EnvironmentResponse> getEnvironmentsByProject(UUID projectId) {
        if (!projectRepo.existsById(projectId)) {
            throw new ProjectNotFoundException("Project", "id", projectId.toString());
        }
        return environmentRepo.findAllByProjectProjectId(projectId).stream()
                .map(environmentMapper::toResponse)
                .toList();
    }

    @Override
    public EnvironmentResponse getEnvironmentById(UUID projectId, UUID environmentId) {
        Environment environment = environmentRepo.findByProjectProjectIdAndEnvironmentId(projectId, environmentId)
                .orElseThrow(() -> new RuntimeException("Environment not found with id " + environmentId + " for project " + projectId));
        return environmentMapper.toResponse(environment);
    }

    @Override
    @Transactional
    public EnvironmentResponse updateEnvironment(UUID projectId, UUID environmentId, EnvironmentDTO request) {
        getActiveProject(projectId);

        Environment environment = environmentRepo.findByProjectProjectIdAndEnvironmentId(projectId, environmentId)
                .orElseThrow(() -> new RuntimeException("Environment not found with id " + environmentId + " for project " + projectId));

        if (!environment.getName().equals(request.getName()) &&
                environmentRepo.existsByProjectProjectIdAndName(projectId, request.getName())) {
            throw new RuntimeException("Environment with name " + request.getName() + " already exists in project " + projectId);
        }

        environmentMapper.updateEntityFromDto(request, environment);
        return environmentMapper.toResponse(environmentRepo.save(environment));
    }

    @Override
    @Transactional
    public void deleteEnvironment(UUID projectId, UUID environmentId) {
        getActiveProject(projectId);
        Environment environment = environmentRepo.findByProjectProjectIdAndEnvironmentId(projectId, environmentId)
                .orElseThrow(() -> new RuntimeException("Environment not found with id " + environmentId + " for project " + projectId));
        environmentRepo.delete(environment);
    }

    private Project getActiveProject(UUID projectId) {
        Project project = projectRepo.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException("Project", "id", projectId.toString()));

        if (project.getProjectStatus() == ProjectStatus.ARCHIVED) {
            throw new RuntimeException("Project " + projectId + " is archived and cannot be modified");
        }
        return project;
    }
}
