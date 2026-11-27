package com.cloudOps.projectService.services.impl;

import com.cloudOps.projectService.constant.GitProvider;
import com.cloudOps.projectService.constant.ProjectStatus;
import com.cloudOps.projectService.dto.SourceRepositoryDTO;
import com.cloudOps.projectService.dto.SourceRepositoryResponse;
import com.cloudOps.projectService.mappers.SourceRepoMapper;
import com.cloudOps.projectService.models.Project;
import com.cloudOps.projectService.models.SourceRepository;
import com.cloudOps.projectService.repository.ProjectRepo;
import com.cloudOps.projectService.repository.SourceRepositoryRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SourceRepoServiceImplTest {

    @Mock
    private SourceRepositoryRepo sourceRepositoryRepo;

    @Mock
    private ProjectRepo projectRepo;

    @Mock
    private SourceRepoMapper sourceRepoMapper;

    @InjectMocks
    private SourceRepoServiceImpl sourceRepoService;

    private UUID projectId;
    private Project project;
    private SourceRepositoryDTO repositoryDTO;
    private SourceRepository repository;
    private SourceRepositoryResponse repositoryResponse;

    @BeforeEach
    void setUp() {
        projectId = UUID.randomUUID();
        project = new Project();
        project.setProjectId(projectId);
        project.setProjectStatus(ProjectStatus.ACTIVE);

        repositoryDTO = SourceRepositoryDTO.builder()
                .repositoryUrl("https://github.com/user/repo")
                .gitProvider(GitProvider.GITHUB)
                .defaultBranch("main")
                .rootDirectory("/")
                .build();

        repository = new SourceRepository();
        repository.setSourceRepositoryId(UUID.randomUUID());
        repository.setRepositoryUrl(repositoryDTO.getRepositoryUrl());
        repository.setProject(project);

        repositoryResponse = SourceRepositoryResponse.builder()
                .sourceRepositoryId(repository.getSourceRepositoryId())
                .repositoryUrl(repository.getRepositoryUrl())
                .build();
    }

    @Test
    void createOrUpdateRepository_Create_Success() {
        when(projectRepo.findById(projectId)).thenReturn(Optional.of(project));
        when(sourceRepositoryRepo.findByProjectProjectId(projectId)).thenReturn(Optional.empty());
        when(sourceRepoMapper.toEntity(repositoryDTO)).thenReturn(repository);
        when(sourceRepositoryRepo.save(any(SourceRepository.class))).thenReturn(repository);
        when(sourceRepoMapper.toResponse(repository)).thenReturn(repositoryResponse);

        SourceRepositoryResponse result = sourceRepoService.createOrUpdateRepository(projectId, repositoryDTO);

        assertNotNull(result);
        verify(sourceRepositoryRepo).save(any(SourceRepository.class));
    }

    @Test
    void createOrUpdateRepository_Update_Success() {
        when(projectRepo.findById(projectId)).thenReturn(Optional.of(project));
        when(sourceRepositoryRepo.findByProjectProjectId(projectId)).thenReturn(Optional.of(repository));
        when(sourceRepositoryRepo.save(any(SourceRepository.class))).thenReturn(repository);
        when(sourceRepoMapper.toResponse(repository)).thenReturn(repositoryResponse);

        SourceRepositoryResponse result = sourceRepoService.createOrUpdateRepository(projectId, repositoryDTO);

        assertNotNull(result);
        verify(sourceRepoMapper).updateEntityFromDto(eq(repositoryDTO), eq(repository));
        verify(sourceRepositoryRepo).save(repository);
    }

    @Test
    void createOrUpdateRepository_InvalidProvider_ThrowsException() {
        repositoryDTO.setGitProvider(GitProvider.GITLAB);
        when(projectRepo.findById(projectId)).thenReturn(Optional.of(project));

        assertThrows(RuntimeException.class, () -> {
            sourceRepoService.createOrUpdateRepository(projectId, repositoryDTO);
        });
    }

    @Test
    void getRepository_Success() {
        when(projectRepo.existsById(projectId)).thenReturn(true);
        when(sourceRepositoryRepo.findByProjectProjectId(projectId)).thenReturn(Optional.of(repository));
        when(sourceRepoMapper.toResponse(repository)).thenReturn(repositoryResponse);

        SourceRepositoryResponse result = sourceRepoService.getRepository(projectId);

        assertNotNull(result);
        assertEquals(repository.getRepositoryUrl(), result.getRepositoryUrl());
    }

    @Test
    void deleteRepository_Success() {
        when(projectRepo.findById(projectId)).thenReturn(Optional.of(project));

        sourceRepoService.deleteRepository(projectId);

        verify(sourceRepositoryRepo).deleteByProjectProjectId(projectId);
    }
}
