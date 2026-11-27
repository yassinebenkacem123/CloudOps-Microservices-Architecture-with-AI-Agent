package com.cloudOps.projectService.dto;

import com.cloudOps.projectService.constant.EnvironmentType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EnvironmentResponse {
    private UUID environmentId;
    private String name;
    private EnvironmentType type;
    private String branch;
    private boolean autoDeploy;
    private Instant createdAt;
    private Instant updatedAt;
}
