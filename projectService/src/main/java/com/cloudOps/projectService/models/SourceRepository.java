package com.cloudOps.projectService.models;

import com.cloudOps.projectService.constant.GitProvider;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SourceRepository {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID sourceRepositoryId;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "project_id",
            nullable = false,
            unique = true
    )
    private Project project;

    @Column(nullable = false)
    private String repositoryUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GitProvider gitProvider;

    private String defaultBranch = "main";

    private String rootDirectory = "/";

    private String credentialRefrence;

}
