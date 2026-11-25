# Security

Validate all inputs; enforce object and organization authorization on every resource operation before public exposure. UUIDs are not authorization. Derive user identity from verified authentication when available; do not trust client ownership claims. Local dev placeholders must remain local. Never log tokens or secrets; store credential references, not plaintext. Prevent SSRF when handling repository URLs or remote integrations; allowlist expected destinations and validate redirects. Use least privilege and avoid exposing internal exceptions.
