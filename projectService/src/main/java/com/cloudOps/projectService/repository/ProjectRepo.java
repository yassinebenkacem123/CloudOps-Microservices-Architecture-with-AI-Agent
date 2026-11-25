package com.cloudOps.projectService.repository;

import com.cloudOps.projectService.models.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProjectRepo extends JpaRepository<Project, UUID> {
}
