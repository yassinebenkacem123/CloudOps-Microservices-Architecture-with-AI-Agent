# Skill: GitHub Actions for CloudOps

## Overview
GitHub Actions is used for Continuous Integration (CI) across the CloudOps monorepo. We use a reusable workflow architecture to minimize duplication.

## Core Workflows
- `reusable-java-ci.yml`: Standardized workflow for Java/Maven services.
    - Inputs: `service_directory`, `java_version`.
    - Steps: Checkout, JDK Setup, Maven Verify, Report Upload.

## Creating a New Service Pipeline
1. Create `.github/workflows/<service-name>-ci.yml`.
2. Configure `on.push.paths` and `on.pull_request.paths` to include the service directory.
3. Call the reusable workflow:
   ```yaml
   jobs:
     call-ci:
       uses: ./.github/workflows/reusable-java-ci.yml
       with:
         service_directory: "your-service"
   ```

## Best Practices
- **Path Filtering**: Always use `paths` to avoid unnecessary builds.
- **Maven Wrapper**: Use `./mvnw` if present for consistent builds.
- **Test Reporting**: Always upload `target/surefire-reports/` and `target/failsafe-reports/`.
- **Environment Isolation**: Ensure tests run with `-Dspring.profiles.active=test` and disable external cloud dependencies.
