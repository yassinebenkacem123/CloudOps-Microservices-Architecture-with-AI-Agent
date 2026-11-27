package com.cloudOps.projectService.services;

import com.cloudOps.projectService.dto.SourceRepositoryDTO;
import com.cloudOps.projectService.dto.SourceRepositoryResponse;

import java.util.UUID;

public interface ISourceRepoService {
    SourceRepositoryResponse createOrUpdateRepository(UUID projectId, SourceRepositoryDTO request);
    SourceRepositoryResponse getRepository(UUID projectId);
    void deleteRepository(UUID projectId);
}
