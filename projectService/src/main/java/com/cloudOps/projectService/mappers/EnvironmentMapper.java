package com.cloudOps.projectService.mappers;

import com.cloudOps.projectService.dto.EnvironmentDTO;
import com.cloudOps.projectService.dto.EnvironmentResponse;
import com.cloudOps.projectService.models.Environment;
import org.springframework.stereotype.Component;

@Component
public class EnvironmentMapper {

    public Environment toEntity(EnvironmentDTO dto) {
        if (dto == null) return null;
        Environment environment = new Environment();
        environment.setName(dto.getName());
        environment.setType(dto.getType());
        environment.setBranch(dto.getBranch());
        environment.setAutoDeploy(dto.isAutoDeploy());
        return environment;
    }

    public EnvironmentResponse toResponse(Environment environment) {
        if (environment == null) return null;
        return EnvironmentResponse.builder()
                .environmentId(environment.getEnvironmentId())
                .name(environment.getName())
                .type(environment.getType())
                .branch(environment.getBranch())
                .autoDeploy(environment.isAutoDeploy())
                .createdAt(environment.getCreatedAt())
                .updatedAt(environment.getUpdatedAt())
                .build();
    }

    public void updateEntityFromDto(EnvironmentDTO dto, Environment environment) {
        if (dto == null || environment == null) return;
        environment.setName(dto.getName());
        environment.setType(dto.getType());
        environment.setBranch(dto.getBranch());
        environment.setAutoDeploy(dto.isAutoDeploy());
    }
}
