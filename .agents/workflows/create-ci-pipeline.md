# Workflow: Create CI Pipeline for a Microservice

## Purpose
Standardize the creation of CI pipelines for new microservices in the CloudOps monorepo.

## Steps

### 1. Verify Test Readiness
- Ensure the service has unit tests (`*Test.java`) and integration tests (`*IT.java`).
- Verify that `mvn clean verify` runs successfully locally.
- Ensure integration tests use an isolated database (H2 or Testcontainers).
- Ensure cloud dependencies (Config Server, Discovery) are disabled for the `test` profile.

### 2. Configure Maven Failsafe
In the service's `pom.xml`, add the `maven-failsafe-plugin` to the `<build><plugins>` section:
```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-failsafe-plugin</artifactId>
    <version>3.2.5</version>
    <executions>
        <execution>
            <goals>
                <goal>integration-test</goal>
                <goal>verify</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```

### 3. Create the Caller Workflow
Create `.github/workflows/<service-directory>-ci.yml`:
1. Define triggers (`push`, `pull_request`).
2. Add `paths` filters for the service directory and relevant configs.
3. Call `reusable-java-ci.yml`.

### 4. Verification
- Validate YAML syntax.
- Push to a feature branch and monitor the Actions tab.
- Verify that both unit and integration tests are executed.
- Verify that test reports are available as artifacts.
