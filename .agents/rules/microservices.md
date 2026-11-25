# Microservices

Keep service ownership explicit. No shared business tables or cross-service JPA associations. Use bounded timeouts for remote calls; do not retry non-idempotent writes blindly. Config Server holds non-secret configuration; secrets come from environment or approved secret store. Gateway routing and load balancing follow direct service verification. Do not add distributed infrastructure prematurely.
