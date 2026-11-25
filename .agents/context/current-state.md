# Current State

Conversation-based baseline (not a repository audit): Config Server, Eureka and Gateway were reported set up. Project Service REST CRUD is in progress; direct endpoint testing precedes load balancing. Proposed entities: Project, SourceRepository, Environment; UUID identifiers; backend-generated slug unique per organization; archive rather than hard delete. Verify actual fields, packages and implementation before coding. Authentication and tenant authorization are not yet confirmed; local dev identity is not production-safe. Do not mark work complete without evidence.
