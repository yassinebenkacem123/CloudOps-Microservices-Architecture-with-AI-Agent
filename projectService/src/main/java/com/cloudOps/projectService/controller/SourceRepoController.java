package com.cloudOps.projectService.controller;

import com.cloudOps.projectService.dto.SourceRepositoryDTO;
import com.cloudOps.projectService.dto.SourceRepositoryResponse;
import com.cloudOps.projectService.services.ISourceRepoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/projects/{projectId}/repository")
@RequiredArgsConstructor
public class SourceRepoController {

    private final ISourceRepoService sourceRepoService;

    @PutMapping
    public SourceRepositoryResponse createOrUpdateRepository(
            @PathVariable UUID projectId,
            @Valid @RequestBody SourceRepositoryDTO request) {
        return sourceRepoService.createOrUpdateRepository(projectId, request);
    }

    @GetMapping
    public SourceRepositoryResponse getRepository(@PathVariable UUID projectId) {
        return sourceRepoService.getRepository(projectId);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRepository(@PathVariable UUID projectId) {
        sourceRepoService.deleteRepository(projectId);
    }
}
