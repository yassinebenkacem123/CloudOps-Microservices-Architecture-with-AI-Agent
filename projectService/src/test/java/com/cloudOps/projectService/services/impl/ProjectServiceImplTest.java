package com.cloudOps.projectService.services.impl;

import com.cloudOps.projectService.dto.CreateProjectRequest;
import com.cloudOps.projectService.dto.ProjectResponse;
import com.cloudOps.projectService.exception.ProjectAlreadyExistsException;
import com.cloudOps.projectService.mappers.ProjectMapper;
import com.cloudOps.projectService.models.Project;
import com.cloudOps.projectService.repository.ProjectRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProjectServiceImplTest {

    @Mock
    private ProjectRepo projectRepo;

    @Mock
    private ProjectMapper projectMapper;

    @InjectMocks
    private ProjectServiceImpl projectService;

    private CreateProjectRequest request;
    private Project project;
    private ProjectResponse response;
    private UUID orgId;

    @BeforeEach
    void setUp() {
        orgId = UUID.randomUUID();
        request = CreateProjectRequest.builder()
                .name("Test Project")
                .slug("test-project")
                .organizationId(orgId)
                .description("Description")
                .build();

        project = new Project();
        project.setProjectId(UUID.randomUUID());
        project.setName(request.getName());
        project.setSlug(request.getSlug());
        project.setOrganizationId(orgId);

        response = new ProjectResponse();
        response.setProjectId(project.getProjectId());
        response.setName(project.getName());
        response.setSlug(project.getSlug());
        response.setOrganizationId(orgId);
    }

    @Test
    void createProject_Success() {
        when(projectRepo.existsByOrganizationIdAndSlug(orgId, "test-project")).thenReturn(false);
        when(projectMapper.toEntity(request)).thenReturn(project);
        when(projectRepo.save(any(Project.class))).thenReturn(project);
        when(projectMapper.toResponse(project)).thenReturn(response);

        ProjectResponse result = projectService.createProject(request, "test-user");

        assertNotNull(result);
        assertEquals(project.getProjectId(), result.getProjectId());
        assertEquals("test-user", result.getCreatedBy());
        verify(projectRepo).save(any(Project.class));
    }

    @Test
    void createProject_AlreadyExists_ThrowsException() {
        when(projectRepo.existsByOrganizationIdAndSlug(orgId, "test-project")).thenReturn(true);

        assertThrows(ProjectAlreadyExistsException.class, () -> {
            projectService.createProject(request, "test-user");
        });

        verify(projectRepo, never()).save(any(Project.class));
    }
}
