package com.cloudOps.projectService.dto;

import com.cloudOps.projectService.constant.EnvironmentType;
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
public class EnvironmentDTO {

    @NotBlank
    @Size(max = 100)
    private String name;

    @NotNull
    private EnvironmentType type;

    @NotBlank
    @Size(max = 255)
    private String branch;

    private boolean autoDeploy;
}
