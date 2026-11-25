package com.cloudOps.projectService.repository;

import com.cloudOps.projectService.models.Environment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface EnvironmentRepo extends JpaRepository<Environment, UUID> {
}
