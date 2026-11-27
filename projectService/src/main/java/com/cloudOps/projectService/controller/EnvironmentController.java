package com.cloudOps.projectService.controller;

import com.cloudOps.projectService.dto.EnvironmentDTO;
import com.cloudOps.projectService.dto.EnvironmentResponse;
import com.cloudOps.projectService.services.IEnvironmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/projects/{projectId}/environments")
@RequiredArgsConstructor
public class EnvironmentController {

    private final IEnvironmentService environmentService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EnvironmentResponse createEnvironment(
            @PathVariable UUID projectId,
            @Valid @RequestBody EnvironmentDTO request) {
        return environmentService.createEnvironment(projectId, request);
    }

    @GetMapping
    public List<EnvironmentResponse> getEnvironments(@PathVariable UUID projectId) {
        return environmentService.getEnvironmentsByProject(projectId);
    }

    @GetMapping("/{environmentId}")
    public EnvironmentResponse getEnvironment(
            @PathVariable UUID projectId,
            @PathVariable UUID environmentId) {
        return environmentService.getEnvironmentById(projectId, environmentId);
    }

    @PutMapping("/{environmentId}")
    public EnvironmentResponse updateEnvironment(
            @PathVariable UUID projectId,
            @PathVariable UUID environmentId,
            @Valid @RequestBody EnvironmentDTO request) {
        return environmentService.updateEnvironment(projectId, environmentId, request);
    }

    @DeleteMapping("/{environmentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteEnvironment(
            @PathVariable UUID projectId,
            @PathVariable UUID environmentId) {
        environmentService.deleteEnvironment(projectId, environmentId);
    }
}
