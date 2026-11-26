package com.cloudOps.projectService.controller;

import com.cloudOps.projectService.dto.CreateProjectRequest;
import com.cloudOps.projectService.dto.ProjectResponse;
import com.cloudOps.projectService.services.IProjectService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ProjectController.class, properties = "spring.cloud.config.enabled=false")
@ActiveProfiles("test")
class ProjectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IProjectService projectService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createProject_Success() throws Exception {
        UUID orgId = UUID.randomUUID();
        UUID projectId = UUID.randomUUID();
        CreateProjectRequest request = new CreateProjectRequest();
        request.setName("New Project");
        request.setSlug("new-project");
        request.setOrganizationId(orgId);

        ProjectResponse response = new ProjectResponse();
        response.setProjectId(projectId);
        response.setName("New Project");
        response.setSlug("new-project");
        response.setOrganizationId(orgId);

        when(projectService.createProject(any(CreateProjectRequest.class), eq("dev-user")))
                .thenReturn(response);

        mockMvc.perform(post("/api/v1/project/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.projectId").value(projectId.toString()))
                .andExpect(jsonPath("$.name").value("New Project"));
    }

    @Test
    void createProject_InvalidRequest_ReturnsBadRequest() throws Exception {
        CreateProjectRequest request = new CreateProjectRequest();
        request.setName(""); // Invalid: Blank
        request.setSlug("Invalid Slug"); // Invalid: Pattern

        mockMvc.perform(post("/api/v1/project/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getProjectsByOrganization_Success() throws Exception {
        UUID orgId = UUID.randomUUID();
        ProjectResponse response = new ProjectResponse();
        response.setProjectId(UUID.randomUUID());
        response.setName("Org Project");
        response.setOrganizationId(orgId);

        when(projectService.getProjectsByOrganization(orgId)).thenReturn(List.of(response));

        mockMvc.perform(get("/api/v1/project")
                        .param("organizationId", orgId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].name").value("Org Project"));
    }
}
