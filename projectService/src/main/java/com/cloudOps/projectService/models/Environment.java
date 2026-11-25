package com.cloudOps.projectService.models;

import com.cloudOps.projectService.constant.EnvironmentType;
import com.cloudOps.projectService.constant.GitProvider;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(
        name = "environments",
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {"project_id", "name"}
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Environment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID environmentId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EnvironmentType type;

    private String branch;

    private boolean autoDeploy = false;


    @CreationTimestamp
    private Instant createdAt;


    @UpdateTimestamp
    private Instant updatedAt;


}
