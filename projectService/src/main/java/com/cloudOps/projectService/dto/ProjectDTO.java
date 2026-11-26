package com.cloudOps.projectService.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectDTO {
    @NotBlank(message = "Project name is required")
    @Size(max = 100)
    private String name;

    @NotBlank(message = "Project slug is required")
    @Pattern(regexp = "^[a-z0-9]+(-[a-z0-9]+)*$")
    @Size(max = 100)
    private String slug;

    @Size(max = 1000)
    private String description;

    @NotNull(message = "Organization ID is required")
    private UUID organizationId;
}
