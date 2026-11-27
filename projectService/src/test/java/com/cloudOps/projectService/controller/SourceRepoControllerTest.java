package com.cloudOps.projectService.controller;

import com.cloudOps.projectService.constant.GitProvider;
import com.cloudOps.projectService.dto.SourceRepositoryDTO;
import com.cloudOps.projectService.dto.SourceRepositoryResponse;
import com.cloudOps.projectService.services.ISourceRepoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = SourceRepoController.class, properties = "spring.cloud.config.enabled=false")
@ActiveProfiles("test")
class SourceRepoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ISourceRepoService sourceRepoService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createOrUpdateRepository_Success() throws Exception {
        UUID projectId = UUID.randomUUID();
        SourceRepositoryDTO request = SourceRepositoryDTO.builder()
                .repositoryUrl("https://github.com/user/repo")
                .gitProvider(GitProvider.GITHUB)
                .build();

        SourceRepositoryResponse response = SourceRepositoryResponse.builder()
                .repositoryUrl("https://github.com/user/repo")
                .build();

        when(sourceRepoService.createOrUpdateRepository(eq(projectId), any(SourceRepositoryDTO.class)))
                .thenReturn(response);

        mockMvc.perform(put("/api/v1/projects/" + projectId + "/repository")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.repositoryUrl").value("https://github.com/user/repo"));
    }

    @Test
    void getRepository_Success() throws Exception {
        UUID projectId = UUID.randomUUID();
        SourceRepositoryResponse response = SourceRepositoryResponse.builder()
                .repositoryUrl("https://github.com/user/repo")
                .build();

        when(sourceRepoService.getRepository(projectId)).thenReturn(response);

        mockMvc.perform(get("/api/v1/projects/" + projectId + "/repository"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.repositoryUrl").value("https://github.com/user/repo"));
    }
}
