package com.cloudOps.projectService.dto;

import com.cloudOps.projectService.constant.GitProvider;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SourceRepositoryDTO {

    @NotBlank(message = "Repository URL is required")
    @Size(max = 255)
    private String repositoryUrl;

    @NotNull(message = "Git provider is required")
    private GitProvider gitProvider;

    @Size(max = 100)
    private String defaultBranch;

    @Size(max = 255)
    private String rootDirectory;

    @Size(max = 255)
    private String credentialRefrence;
}
