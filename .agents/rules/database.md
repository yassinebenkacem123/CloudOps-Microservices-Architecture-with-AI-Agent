# Database

Use PostgreSQL and UUID identifiers where already chosen. Local ddl-auto update is accepted; never use create-drop against persistent data. Preserve unique constraints, foreign keys within service boundaries, and optimistic locking where applicable. Test real constraints and concurrent conflicts where relevant. No Flyway unless user changes decision. Do not alter or delete existing data without approval. Production schema evolution remains unresolved.
