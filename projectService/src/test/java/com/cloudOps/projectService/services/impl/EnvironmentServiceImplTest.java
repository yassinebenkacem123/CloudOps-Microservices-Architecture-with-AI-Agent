package com.cloudOps.projectService.services.impl;

import com.cloudOps.projectService.constant.EnvironmentType;
import com.cloudOps.projectService.constant.ProjectStatus;
import com.cloudOps.projectService.dto.EnvironmentDTO;
import com.cloudOps.projectService.dto.EnvironmentResponse;
import com.cloudOps.projectService.exception.ProjectNotFoundException;
import com.cloudOps.projectService.mappers.EnvironmentMapper;
import com.cloudOps.projectService.models.Environment;
import com.cloudOps.projectService.models.Project;
import com.cloudOps.projectService.repository.EnvironmentRepo;
import com.cloudOps.projectService.repository.ProjectRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EnvironmentServiceImplTest {

    @Mock
    private EnvironmentRepo environmentRepo;

    @Mock
    private ProjectRepo projectRepo;

    @Mock
    private EnvironmentMapper environmentMapper;

    @InjectMocks
    private EnvironmentServiceImpl environmentService;

    private UUID projectId;
    private Project project;
    private EnvironmentDTO environmentDTO;
    private Environment environment;
    private EnvironmentResponse environmentResponse;

    @BeforeEach
    void setUp() {
        projectId = UUID.randomUUID();
        project = new Project();
        project.setProjectId(projectId);
        project.setProjectStatus(ProjectStatus.ACTIVE);

        environmentDTO = EnvironmentDTO.builder()
                .name("Production")
                .type(EnvironmentType.PRODUCTION)
                .branch("main")
                .autoDeploy(true)
                .build();

        environment = new Environment();
        environment.setEnvironmentId(UUID.randomUUID());
        environment.setName("Production");
        environment.setProject(project);

        environmentResponse = EnvironmentResponse.builder()
                .environmentId(environment.getEnvironmentId())
                .name("Production")
                .build();
    }

    @Test
    void createEnvironment_Success() {
        when(projectRepo.findById(projectId)).thenReturn(Optional.of(project));
        when(environmentRepo.existsByProjectProjectIdAndName(projectId, environmentDTO.getName())).thenReturn(false);
        when(environmentMapper.toEntity(environmentDTO)).thenReturn(environment);
        when(environmentRepo.save(any(Environment.class))).thenReturn(environment);
        when(environmentMapper.toResponse(environment)).thenReturn(environmentResponse);

        EnvironmentResponse result = environmentService.createEnvironment(projectId, environmentDTO);

        assertNotNull(result);
        assertEquals("Production", result.getName());
        verify(environmentRepo).save(any(Environment.class));
    }

    @Test
    void createEnvironment_ProjectArchived_ThrowsException() {
        project.setProjectStatus(ProjectStatus.ARCHIVED);
        when(projectRepo.findById(projectId)).thenReturn(Optional.of(project));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            environmentService.createEnvironment(projectId, environmentDTO);
        });

        assertTrue(exception.getMessage().contains("archived"));
        verify(environmentRepo, never()).save(any(Environment.class));
    }

    @Test
    void getEnvironmentsByProject_Success() {
        when(projectRepo.existsById(projectId)).thenReturn(true);
        when(environmentRepo.findAllByProjectProjectId(projectId)).thenReturn(List.of(environment));
        when(environmentMapper.toResponse(environment)).thenReturn(environmentResponse);

        List<EnvironmentResponse> results = environmentService.getEnvironmentsByProject(projectId);

        assertNotNull(results);
        assertEquals(1, results.size());
    }

    @Test
    void updateEnvironment_DuplicateName_ThrowsException() {
        UUID envId = environment.getEnvironmentId();
        EnvironmentDTO updateDTO = EnvironmentDTO.builder().name("Staging").build();
        
        when(projectRepo.findById(projectId)).thenReturn(Optional.of(project));
        when(environmentRepo.findByProjectProjectIdAndEnvironmentId(projectId, envId)).thenReturn(Optional.of(environment));
        when(environmentRepo.existsByProjectProjectIdAndName(projectId, "Staging")).thenReturn(true);

        assertThrows(RuntimeException.class, () -> {
            environmentService.updateEnvironment(projectId, envId, updateDTO);
        });
    }

    @Test
    void deleteEnvironment_Success() {
        UUID envId = environment.getEnvironmentId();
        when(projectRepo.findById(projectId)).thenReturn(Optional.of(project));
        when(environmentRepo.findByProjectProjectIdAndEnvironmentId(projectId, envId)).thenReturn(Optional.of(environment));

        environmentService.deleteEnvironment(projectId, envId);

        verify(environmentRepo).delete(environment);
    }
}
