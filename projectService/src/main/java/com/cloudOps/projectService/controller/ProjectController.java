package com.cloudOps.projectService.controller;


import com.cloudOps.projectService.dto.CreateProjectRequest;
import com.cloudOps.projectService.dto.ProjectResponse;
import com.cloudOps.projectService.models.Project;
import com.cloudOps.projectService.services.IProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/project")
public class ProjectController {

    private final IProjectService projectService;

    @PostMapping("/create")
    public ResponseEntity<ProjectResponse> createProject(
            @Valid @RequestBody CreateProjectRequest request
    ){
        String createdBy = "dev-user"; // i will change it when keycloak is integrated

        ProjectResponse projectResponse = projectService.createProject(request, createdBy);

        URI location = URI.create("/api/v1/project/create" + projectResponse.getProjectId());
        return ResponseEntity.created(location).body(projectResponse);
    }



}
