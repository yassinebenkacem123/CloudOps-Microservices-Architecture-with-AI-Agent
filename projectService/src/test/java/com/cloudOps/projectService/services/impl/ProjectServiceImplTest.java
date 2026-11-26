package com.cloudOps.projectService.services.impl;

import com.cloudOps.projectService.dto.ProjectDTO;
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

    private ProjectDTO request;
    private Project project;
    private ProjectResponse response;
    private UUID orgId;

    @BeforeEach
    void setUp() {
        orgId = UUID.randomUUID();
        request = new ProjectDTO();
        request.setName("Test Project");
        request.setSlug("test-project");
        request.setOrganizationId(orgId);
        request.setDescription("Description");

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

    @Test
    void getProjectById_Success() {
        UUID projectId = project.getProjectId();
        when(projectRepo.findById(projectId)).thenReturn(java.util.Optional.of(project));
        when(projectMapper.toResponse(project)).thenReturn(response);

        ProjectResponse result = projectService.getProjectById(projectId);

        assertNotNull(result);
        assertEquals(projectId, result.getProjectId());
        verify(projectRepo).findById(projectId);
    }

    @Test
    void getProjectById_NotFound_ThrowsException() {
        UUID projectId = UUID.randomUUID();
        when(projectRepo.findById(projectId)).thenReturn(java.util.Optional.empty());

        assertThrows(com.cloudOps.projectService.exception.ProjectNotFoundException.class, () -> {
            projectService.getProjectById(projectId);
        });
    }

    @Test
    void getProjectsByOrganization_Success() {
        when(projectRepo.findAllByOrganizationId(orgId)).thenReturn(java.util.List.of(project));
        when(projectMapper.toResponse(project)).thenReturn(response);

        java.util.List<ProjectResponse> result = projectService.getProjectsByOrganization(orgId);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(project.getProjectId(), result.get(0).getProjectId());
        verify(projectRepo).findAllByOrganizationId(orgId);
    }

    @Test
    void updateProject_Success() {
        UUID projectId = project.getProjectId();
        ProjectDTO updateRequest = new ProjectDTO();
        updateRequest.setName("Updated Name");
        updateRequest.setSlug("updated-slug");
        updateRequest.setOrganizationId(orgId);

        when(projectRepo.findById(projectId)).thenReturn(java.util.Optional.of(project));
        when(projectRepo.existsByOrganizationIdAndSlug(orgId, "updated-slug")).thenReturn(false);
        when(projectRepo.save(any(Project.class))).thenReturn(project);
        when(projectMapper.toResponse(any(Project.class))).thenReturn(response);

        ProjectResponse result = projectService.updateProject(projectId, updateRequest);

        assertNotNull(result);
        verify(projectRepo).save(any(Project.class));
        verify(projectMapper).updateEntityFromDto(eq(updateRequest), any(Project.class));
    }
}
