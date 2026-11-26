package com.cloudOps.projectService;

import com.cloudOps.projectService.dto.CreateProjectRequest;
import com.cloudOps.projectService.repository.ProjectRepo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ProjectIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProjectRepo projectRepo;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        projectRepo.deleteAll();
    }

    @Test
    void shouldCreateProjectSuccessfully() throws Exception {
        UUID orgId = UUID.randomUUID();
        CreateProjectRequest request = new CreateProjectRequest();
        request.setName("Integration Project");
        request.setSlug("int-project");
        request.setOrganizationId(orgId);
        request.setDescription("Integration Test Description");

        mockMvc.perform(post("/api/v1/project/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Integration Project"))
                .andExpect(jsonPath("$.slug").value("int-project"))
                .andExpect(jsonPath("$.organizationId").value(orgId.toString()));
    }

    @Test
    void shouldReturnConflictWhenProjectAlreadyExists() throws Exception {
        UUID orgId = UUID.randomUUID();
        CreateProjectRequest request = new CreateProjectRequest();
        request.setName("Existing Project");
        request.setSlug("existing-project");
        request.setOrganizationId(orgId);

        // First creation
        mockMvc.perform(post("/api/v1/project/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        // Second creation with same slug and orgId
        mockMvc.perform(post("/api/v1/project/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}
