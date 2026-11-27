package com.cloudOps.projectService.services;

import com.cloudOps.projectService.dto.EnvironmentDTO;
import com.cloudOps.projectService.dto.EnvironmentResponse;

import java.util.List;
import java.util.UUID;

public interface IEnvironmentService {
    EnvironmentResponse createEnvironment(UUID projectId, EnvironmentDTO request);
    List<EnvironmentResponse> getEnvironmentsByProject(UUID projectId);
    EnvironmentResponse getEnvironmentById(UUID projectId, UUID environmentId);
    EnvironmentResponse updateEnvironment(UUID projectId, UUID environmentId, EnvironmentDTO request);
    void deleteEnvironment(UUID projectId, UUID environmentId);
}
