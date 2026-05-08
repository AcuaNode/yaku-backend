---
trigger: always_on
---

# Estandarización de Clean Architecture: Estructura, Nomenclatura y Reglas para microservicio Java

## 1. Estructura de Carpetas (Ejemplo)

```
microservice-contributions/
└── src/
    └── main/
        └── java/
            └── com/
                └── fiscalliance/
                    └── contributions/
                        ├── application/
                        │   └── internal/
                        │       ├── commandservices/
                        │       └── queryservices/
                        ├── domain/
                        │   ├── models/
                        │   │   ├── aggregates/
                        │   │   ├── commands/
                        │   │   ├── queries/
                        │   │   └── valueobjects/
                        │   └── services/
                        │       ├── ContributionCommandService.java
                        │       ├── ContributionQueryService.java
                        │       ├── MemberContributionCommandService.java
                        │       └── MemberContributionQueryService.java
                        ├── infrastructure/
                        │   ├── configuration/
                        │   │   └── RestTemplateConfig.java
                        │   ├── persistance/
                        │   │   └── jpa/
                        │   └── rest/
                        │       ├── BillClient.java
                        │       ├── HouseholdClient.java
                        │       └── IamClient.java
                        └── interfaces/
                            └── rest/
                                ├── resources/
                                └── transform/
```






