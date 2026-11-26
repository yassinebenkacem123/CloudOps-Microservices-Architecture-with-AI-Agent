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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

    @Test
    void shouldGetProjectsByOrganization() throws Exception {
        UUID orgId = UUID.randomUUID();
        
        CreateProjectRequest p1 = new CreateProjectRequest();
        p1.setName("Project 1");
        p1.setSlug("p1");
        p1.setOrganizationId(orgId);

        CreateProjectRequest p2 = new CreateProjectRequest();
        p2.setName("Project 2");
        p2.setSlug("p2");
        p2.setOrganizationId(orgId);

        mockMvc.perform(post("/api/v1/project/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(p1)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/v1/project/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(p2)))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/v1/project")
                        .param("organizationId", orgId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].organizationId").value(orgId.toString()))
                .andExpect(jsonPath("$[1].organizationId").value(orgId.toString()));
    }
}
