package com.cloudOps.projectService.dto;

import com.cloudOps.projectService.constant.ProjectStatus;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
public class ProjectResponse {
    UUID projectId;
    String name;
    String slug;
    String description;
    UUID organizationId;
    String createdBy;
    ProjectStatus status;
    Instant createdAt;
    Instant updatedAt;
}
