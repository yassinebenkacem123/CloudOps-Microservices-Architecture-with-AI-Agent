package com.cloudOps.projectService.mappers;

import com.cloudOps.projectService.dto.SourceRepositoryDTO;
import com.cloudOps.projectService.dto.SourceRepositoryResponse;
import com.cloudOps.projectService.models.SourceRepository;
import org.springframework.stereotype.Component;

@Component
public class SourceRepoMapper {

    public SourceRepository toEntity(SourceRepositoryDTO dto) {
        if (dto == null) return null;
        SourceRepository repository = new SourceRepository();
        repository.setRepositoryUrl(dto.getRepositoryUrl());
        repository.setGitProvider(dto.getGitProvider());
        repository.setDefaultBranch(dto.getDefaultBranch() != null ? dto.getDefaultBranch() : "main");
        repository.setRootDirectory(dto.getRootDirectory() != null ? dto.getRootDirectory() : "/");
        repository.setCredentialRefrence(dto.getCredentialRefrence());
        return repository;
    }

    public SourceRepositoryResponse toResponse(SourceRepository repository) {
        if (repository == null) return null;
        return SourceRepositoryResponse.builder()
                .sourceRepositoryId(repository.getSourceRepositoryId())
                .repositoryUrl(repository.getRepositoryUrl())
                .gitProvider(repository.getGitProvider())
                .defaultBranch(repository.getDefaultBranch())
                .rootDirectory(repository.getRootDirectory())
                .credentialRefrence(repository.getCredentialRefrence())
                .build();
    }

    public void updateEntityFromDto(SourceRepositoryDTO dto, SourceRepository repository) {
        if (dto == null || repository == null) return;
        repository.setRepositoryUrl(dto.getRepositoryUrl());
        repository.setGitProvider(dto.getGitProvider());
        if (dto.getDefaultBranch() != null) repository.setDefaultBranch(dto.getDefaultBranch());
        if (dto.getRootDirectory() != null) repository.setRootDirectory(dto.getRootDirectory());
        repository.setCredentialRefrence(dto.getCredentialRefrence());
    }
}
