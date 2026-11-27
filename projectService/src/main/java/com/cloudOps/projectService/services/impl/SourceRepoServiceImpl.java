package com.cloudOps.projectService.services.impl;

import com.cloudOps.projectService.constant.GitProvider;
import com.cloudOps.projectService.constant.ProjectStatus;
import com.cloudOps.projectService.dto.SourceRepositoryDTO;
import com.cloudOps.projectService.dto.SourceRepositoryResponse;
import com.cloudOps.projectService.exception.ProjectNotFoundException;
import com.cloudOps.projectService.mappers.SourceRepoMapper;
import com.cloudOps.projectService.models.Project;
import com.cloudOps.projectService.models.SourceRepository;
import com.cloudOps.projectService.repository.ProjectRepo;
import com.cloudOps.projectService.repository.SourceRepositoryRepo;
import com.cloudOps.projectService.services.ISourceRepoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SourceRepoServiceImpl implements ISourceRepoService {

    private final SourceRepositoryRepo sourceRepositoryRepo;
    private final ProjectRepo projectRepo;
    private final SourceRepoMapper sourceRepoMapper;

    @Override
    @Transactional
    public SourceRepositoryResponse createOrUpdateRepository(UUID projectId, SourceRepositoryDTO request) {
        Project project = getActiveProject(projectId);

        if (request.getGitProvider() != GitProvider.GITHUB) {
            throw new RuntimeException("Only GitHub is supported at this moment");
        }

        validateRepositoryUrl(request.getRepositoryUrl(), request.getGitProvider());

        SourceRepository repository = sourceRepositoryRepo.findByProjectProjectId(projectId)
                .map(existing -> {
                    sourceRepoMapper.updateEntityFromDto(request, existing);
                    return existing;
                })
                .orElseGet(() -> {
                    SourceRepository newRepo = sourceRepoMapper.toEntity(request);
                    newRepo.setProject(project);
                    return newRepo;
                });

        return sourceRepoMapper.toResponse(sourceRepositoryRepo.save(repository));
    }

    @Override
    public SourceRepositoryResponse getRepository(UUID projectId) {
        if (!projectRepo.existsById(projectId)) {
            throw new ProjectNotFoundException("Project", "id", projectId.toString());
        }
        SourceRepository repository = sourceRepositoryRepo.findByProjectProjectId(projectId)
                .orElseThrow(() -> new RuntimeException("Source repository not found for project " + projectId));
        return sourceRepoMapper.toResponse(repository);
    }

    @Override
    @Transactional
    public void deleteRepository(UUID projectId) {
        getActiveProject(projectId);
        sourceRepositoryRepo.deleteByProjectProjectId(projectId);
    }

    private Project getActiveProject(UUID projectId) {
        Project project = projectRepo.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException("Project", "id", projectId.toString()));

        if (project.getProjectStatus() == ProjectStatus.ARCHIVED) {
            throw new RuntimeException("Project " + projectId + " is archived and cannot be modified");
        }
        return project;
    }

    private void validateRepositoryUrl(String url, GitProvider provider) {
        if (provider == GitProvider.GITHUB && !url.contains("github.com")) {
            throw new RuntimeException("Invalid repository URL for GitHub provider");
        }
        // Basic URL pattern validation can be added here if needed
    }
}
