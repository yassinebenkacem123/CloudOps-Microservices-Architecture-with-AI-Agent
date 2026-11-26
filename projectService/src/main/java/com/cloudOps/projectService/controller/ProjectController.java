package com.cloudOps.projectService.controller;


import com.cloudOps.projectService.dto.ProjectDTO;
import com.cloudOps.projectService.dto.ProjectResponse;
import com.cloudOps.projectService.services.IProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/project")
public class ProjectController {

    private final IProjectService projectService;

    
    // create project
    @PostMapping("/create")
    public ResponseEntity<ProjectResponse> createProject(
            @Valid @RequestBody ProjectDTO request
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

    // get all projects by organization id
    @GetMapping
    public ResponseEntity<List<ProjectResponse>> getProjects(
            @RequestParam(required = false) UUID organizationId
    ){
        if(organizationId != null){
            return ResponseEntity.ok(
                    projectService.getProjectsByOrganization(organizationId)
            );
        }
        // If no organizationId is provided, we might want to return all projects or handle it differently
        // For now, based on the previous code intent, it seems it wanted to call a method to get all.
        // But IProjectService doesn't have getAllProjects right now. 
        // Let's assume for now we only support fetching by organization if param is present, 
        // or we return empty list if not.
        return ResponseEntity.ok(List.of());
    };


    @PutMapping("/update/{id}")
    public ResponseEntity<ProjectResponse> updateProject(
            @PathVariable("id") UUID projectId,
            @Valid @RequestBody ProjectDTO request
    ){
        return ResponseEntity.ok(
                projectService.updateProject(projectId, request)
        );
    }
}
