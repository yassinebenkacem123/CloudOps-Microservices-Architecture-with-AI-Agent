package com.cloudOps.projectService.repository;

import com.cloudOps.projectService.models.Environment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EnvironmentRepo extends JpaRepository<Environment, UUID> {
    List<Environment> findAllByProjectProjectId(UUID projectId);
    Optional<Environment> findByProjectProjectIdAndEnvironmentId(UUID projectId, UUID environmentId);
    boolean existsByProjectProjectIdAndName(UUID projectId, String name);
}
