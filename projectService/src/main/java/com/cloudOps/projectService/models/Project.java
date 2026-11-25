package com.cloudOps.projectService.models;

import com.cloudOps.projectService.constant.ProjectStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(
        name = "projects",
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {"organization_id", "slug"}
                )
        }
)
@Getter @Setter
@NoArgsConstructor
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID projectId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String slug;

    @Column(length =  1000)
    private String description;


    @Column(name = "organization_id", nullable = false)
    private UUID organizationId;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProjectStatus projectStatus = ProjectStatus.ACTIVE;


    @CreationTimestamp
    private Instant createdAt;


    @UpdateTimestamp
    private Instant updatedAt;

    @Version
    private Long version;





}
