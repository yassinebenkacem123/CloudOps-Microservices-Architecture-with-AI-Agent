# CI/CD Rules

## General Principles
- Every microservice must have its own CI caller workflow.
- Java microservices must reuse the `reusable-java-ci.yml` workflow.
- CI pipelines must execute both unit tests and integration tests.
- All tests must pass for the pipeline to succeed.
- Never disable, skip, or weaken tests to make CI pass.

## Workflow Configuration
- Use path-based filtering to ensure only relevant builds are triggered.
- Triggers should include pushes to `main`, service-specific branches, and pull requests to `main`.
- Reusable workflows must handle:
    - JDK setup and caching.
    - Full Maven lifecycle (`verify`).
    - Artifact upload for test reports (Surefire and Failsafe).

## Security
- Apply least-privilege permissions (`contents: read`).
- Never hardcode secrets or credentials in YAML files.
- Use isolated test databases (e.g., H2 or Testcontainers) for integration tests.
- Ensure CI does not depend on production or shared environment services (Config Server, Eureka).

## Maintenance
- Keep CI configurations DRY by using reusable workflows.
- Document inputs and outputs for reusable workflows.
- Monitor pipeline execution and keep JDK/Actions versions up to date.
