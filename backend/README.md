# Backend Module — Mohsinon

Modular Monolith application built with:
- Java 17 LTS
- Spring Boot 3.x
- Spring Security (JWT + Refresh Tokens)
- Spring Data JPA / Hibernate
- PostgreSQL / H2 Dev Profile
- Flyway Database Migrations
- OpenAPI 3 / Swagger

## Structure
```text
backend/
├── src/main/java/com/mohsinon/
│   ├── MohsinonApplication.java
│   ├── core/                    # Cross-cutting concerns (Security, Audit, Exceptions, BaseEntity)
│   └── modules/                 # Bounded contexts (Identity, Mosques, Donations, Initiatives, Skills, Reputation)
├── src/main/resources/
│   ├── application.yml          # Spring configuration profiles
│   └── db/migration/            # Flyway SQL migrations
└── pom.xml
```
