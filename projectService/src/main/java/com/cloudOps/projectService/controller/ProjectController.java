package com.cloudOps.projectService.controller;


import com.cloudOps.projectService.dto.CreateProjectRequest;
import com.cloudOps.projectService.dto.ProjectResponse;
import com.cloudOps.projectService.models.Project;
import com.cloudOps.projectService.services.IProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/project")
public class ProjectController {

    private final IProjectService projectService;

    
    // create project
    @PostMapping("/create")
    public ResponseEntity<ProjectResponse> createProject(
            @Valid @RequestBody CreateProjectRequest request
    ){
        String createdBy = "dev-user"; // i will change it when keycloak is integrated

        ProjectResponse projectResponse = projectService.createProject(request, createdBy);

        URI location = URI.create("/api/v1/project/create" + projectResponse.getProjectId());
        return ResponseEntity.created(location).body(projectResponse);
    }



    // get project by id

    @PostMapping("/{id}")
    public ResponseEntity<ProjectResponse> getProjectById(
            @PathVariable("id") UUID projectId

    ){
        return ResponseEntity.ok(
                projectService.getProjectById(projectId)
        );
    }

}
