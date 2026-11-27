package com.cloudOps.projectService.dto;

import com.cloudOps.projectService.constant.GitProvider;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SourceRepositoryResponse {
    private UUID sourceRepositoryId;
    private String repositoryUrl;
    private GitProvider gitProvider;
    private String defaultBranch;
    private String rootDirectory;
    private String credentialRefrence;
}
