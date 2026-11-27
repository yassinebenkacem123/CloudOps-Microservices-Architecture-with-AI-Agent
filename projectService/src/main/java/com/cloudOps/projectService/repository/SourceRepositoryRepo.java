package com.cloudOps.projectService.repository;

import com.cloudOps.projectService.models.SourceRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface SourceRepositoryRepo extends JpaRepository<SourceRepository, UUID> {
    Optional<SourceRepository> findByProjectProjectId(UUID projectId);
    void deleteByProjectProjectId(UUID projectId);
}
