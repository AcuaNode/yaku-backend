---
trigger: always_on
---

# Capa application

## ¿Qué se hace en application?

La capa **application** contiene la lógica que orquesta los casos de uso del sistema, coordinando el flujo entre el dominio, los adaptadores externos y los recursos de infraestructura. 
Su objetivo principal es organizar y ejecutar la lógica necesaria para cumplir con los requerimientos de negocio, sin contener lógica de negocio pura (eso corresponde al dominio).

### Responsabilidades clave:
- Orquestar comandos y queries siguiendo CQRS (Command-Query Responsibility Segregation).
- Coordinar la interacción entre servicios de dominio y los adaptadores externos.
- Validar reglas de negocio compuestas, pero no implementar lógica de dominio específica (esa va en domain/services).
- Manejar casos de uso que requieren integración de varios servicios o acciones atómicas.
- Transformar datos entre capas si es necesario.

---

## Estructura típica en tu código

Según tu código y el árbol de carpetas reportado:

```
application/
└── internal/
    ├── commandservices/
    └── queryservices/
```

Pueden existir otras subcarpetas como:
- `command/`
- `query/`

Pero, en tu caso, la convención es tener:
- **commandservices**: Servicios que ejecutan comandos (acciones que modifican el estado).
- **queryservices**: Servicios que ejecutan consultas (acciones de solo lectura).

---

## Nomenclatura de archivos

### En commandservices

- **[Entidad][Verbo]CommandService.java**
  - Ejemplo: `CreateContributionCommandService.java`
  - Ejemplo: `UpdateContributionCommandService.java`
  - Deben ser clases de servicio para comandos; puede haber uno por caso de uso importante.

- **[Entidad]CommandHandler.java**
  - Alternativamente, si manejas los patrones handler.

### En queryservices

- **[Entidad][Verbo]QueryService.java**
  - Ejemplo: `GetContributionDetailsQueryService.java`
  - Ejemplo: `ListContributionsQueryService.java`
  - Son clases de servicio dedicadas a operaciones de consulta.

- **[Entidad]QueryHandler.java**
  - Alternativamente, si trabajas handlers de queries.

---

### Estructura extendida ideal

```plaintext
application/
└── internal/
    ├── commandservices/
    │   ├── CreateContributionCommandService.java
    │   ├── UpdateContributionCommandService.java
    │   └── DeleteContributionCommandService.java
    └── queryservices/
        ├── GetContributionDetailsQueryService.java
        ├── ListContributionsQueryService.java
        └── FindContributionByMemberQueryService.java
```

- En cada carpeta, cada clase debe representar un caso de uso.
- Si el caso de uso es simple y sólo transporta datos, puede usarse un `record` para el input/output del servicio.
- Los archivos usan **PascalCase** y el sufijo especifica su responsabilidad (`CommandService` o `QueryService`).

---

## Ejemplo de archivo

```java
// CreateContributionCommandService.java
package com.fiscalliance.contributions.application.internal.commandservices;

import com.fiscalliance.contributions.domain.models.commands.CreateContributionCommand;
import com.fiscalliance.contributions.domain.services.ContributionCommandService;

public class CreateContributionCommandService {
    private final ContributionCommandService contributionCommandService;

    public CreateContributionCommandService(ContributionCommandService contributionCommandService) {
        this.contributionCommandService = contributionCommandService;
    }

    public Long handle(CreateContributionCommand command) {
        // Orquestación del caso de uso para crear una contribución
        return contributionCommandService.createContribution(
            command.amount(),
            command.memberId()
        );
    }
}
```

---

## Convenciones y mejores prácticas

- Cada **CommandService** orquesta un comando (acción que cambia el estado).
- Cada **QueryService** orquesta una consulta (sólo lectura).
- Los servicios de aplicación NO contienen lógica de negocio profundo, sólo coordinación.
- Los comandos/queries (en domain/models/commands y domain/models/queries) deben ser `record`.
- Usa inyección de dependencias para los servicios de dominio.
- Si surge un patrón repetido usa handlers.
- Mantén los métodos públicos y descriptivos, representando la intención del caso de uso.

---

## Resumen para IA/humanos

1. Cada Command/Query representado en domain/models/[commands|queries].
2. Un Service en [commandservices|queryservices] por caso de uso relevante.
3. Nomenclatura clara, según : `[Entidad][Verbo][Command|Query]Service.java`
4. No poner lógica de dominio aquí, solo coordinación/validación simple.
5. Estructura lista para escalar y facilitar testing/mockeo.

---