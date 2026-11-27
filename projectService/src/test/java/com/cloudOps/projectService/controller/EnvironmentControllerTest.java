package com.cloudOps.projectService.controller;

import com.cloudOps.projectService.constant.EnvironmentType;
import com.cloudOps.projectService.dto.EnvironmentDTO;
import com.cloudOps.projectService.dto.EnvironmentResponse;
import com.cloudOps.projectService.services.IEnvironmentService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = EnvironmentController.class, properties = "spring.cloud.config.enabled=false")
@ActiveProfiles("test")
class EnvironmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IEnvironmentService environmentService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createEnvironment_Success() throws Exception {
        UUID projectId = UUID.randomUUID();
        EnvironmentDTO request = EnvironmentDTO.builder()
                .name("Production")
                .type(EnvironmentType.PRODUCTION)
                .branch("main")
                .build();

        EnvironmentResponse response = EnvironmentResponse.builder()
                .environmentId(UUID.randomUUID())
                .name("Production")
                .build();

        when(environmentService.createEnvironment(eq(projectId), any(EnvironmentDTO.class)))
                .thenReturn(response);

        mockMvc.perform(post("/api/v1/projects/" + projectId + "/environments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Production"));
    }

    @Test
    void getEnvironments_Success() throws Exception {
        UUID projectId = UUID.randomUUID();
        EnvironmentResponse response = EnvironmentResponse.builder().name("Staging").build();

        when(environmentService.getEnvironmentsByProject(projectId)).thenReturn(List.of(response));

        mockMvc.perform(get("/api/v1/projects/" + projectId + "/environments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].name").value("Staging"));
    }
}
