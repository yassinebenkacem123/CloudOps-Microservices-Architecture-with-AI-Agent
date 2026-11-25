---
name: integration-testing
description: Verify real Spring and PostgreSQL boundaries.
---
# Verify real Spring and PostgreSQL boundaries

Inspect test dependencies and existing profiles. Use PostgreSQL Testcontainers with isolated database and dynamic properties or ServiceConnection if supported. Disable external discovery/config imports in tests using version-compatible configuration. Test HTTP → service → repository → database, validation, unique constraints, archive behavior and error mapping. Assert persisted state. Configure test discovery and verify reports. Docker unavailable is a blocker, not a pass.

Follow AGENTS.md and applicable rules. Report verification and blockers.
