# Architecture

Current intended topology: Config Server → Eureka → reactive API Gateway → REST business services. Test Project Service directly before gateway routing. Each business service owns its database; replicas of one service share its database. Cross-service references use IDs, not JPA relationships. Planned deployment, monitoring, identity and AI boundaries are proposals, not verified implementations.
