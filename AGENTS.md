# CloudOps engineering instructions
Read `.agents/context/current-state.md`, `project-overview.md`, `architecture.md`, and `technology-stack.md` before changing code. Inspect actual source, build files and configuration; repository evidence overrides descriptive context. Read all applicable `.agents/rules/` and the relevant skill and workflow before implementation. Never treat future architecture as implemented.

## Mandatory delivery contract
- Make the smallest coherent change; preserve existing conventions and public contracts unless explicitly authorized.
- Every new or changed behavior requires unit tests and applicable integration tests in the same change. Bug fixes require regression tests. For non-executable changes explain why tests are not applicable; never silently omit them.
- Test success, invalid input, missing resources, conflicts and authorization boundaries where applicable. Do not weaken existing tests or mock away the integration boundary.
- Run targeted tests, then the affected module verification lifecycle. Report exact commands, outcomes, and unexecuted checks. Never claim tests passed without execution.
- Do not introduce Flyway, GraphQL, Kafka, Keycloak or infrastructure changes merely because they are planned. Current focus is direct Project REST CRUD; gateway/load balancing follows.
- Never commit secrets, bypass authorization, delete data, push changes or deploy without authorization. Ask before destructive or broad architectural changes.
- Finish using `.agents/checklists/definition-of-done.md` and report assumptions, limitations, and remaining risks.
