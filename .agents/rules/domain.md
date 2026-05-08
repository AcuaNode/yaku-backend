---
trigger: always_on
---

# Capa domain

## ¿Qué se hace en domain?

Esta capa contiene el corazón de la lógica de negocio de tu aplicación.  
Aquí se definen:
- Entidades del dominio (aggregates principales)
- Value Objects (objetos de valor inmutables)
- Interfaces de servicios de dominio y contratos de repositorio
- Comandos y queries (solo sus contratos, normalmente como records)
- Lógica de reglas de negocio crítica y validaciones

**Nunca contiene lógica de infraestructura ni detalles de frameworks.**

---

## Estructura en tu proyecto

Según tu código, la estructura de domain es la siguiente:

```
domain/
├── models/
│   ├── aggregates/
│   ├── commands/
│   ├── queries/
│   └── valueobjects/
└── services/
    ├── ContributionCommandService.java
    ├── ContributionQueryService.java
    ├── MemberContributionCommandService.java
    └── MemberContributionQueryService.java
```

**Nota:** El “aggregate” está bajo una sola carpeta `aggregates`, no en varias.

---

## Nomenclatura de archivos

### En aggregates

- `[Entidad].java`
  - Ejemplo: `Contribution.java`  
  Representa una entidad de dominio/aggregate root.  
  Contiene atributos, lógica fundamental y métodos de negocio.

### En commands

- `[Verbo][Entidad]Command.java`
  - Ejemplo: `CreateContributionCommand.java`, `UpdateContributionCommand.java`
  - Deben ser records (inmutables, solo datos necesarios para la operación).

### En queries

- `[Verbo][Entidad]Query.java`
  - Ejemplo: `GetContributionDetailsQuery.java`, `ListContributionsQuery.java`
  - También deben ser records.

### En valueobjects

- `[NombreVO].java` o `[Entidad][Nombre]VO.java`
  - Ejemplo: `ContributionAmountVO.java`, `MemberIdVO.java`
  - Inmutables, sin identidad propia, reglas de validación embebidas.

### En services

- `[Entidad][Command|Query]Service.java`
  - Ejemplo: `ContributionCommandService.java`, `MemberContributionQueryService.java`
  - Interfaces del dominio (no implementación).
  - Detallan contratos que la infraestructura o application implementarán.
  - Solo lógica muy relevante para dominio va aquí.
- Si tienes métodos por cada caso de uso y el dominio es rico, aquí definen su contrato.

---

## Ejemplo de estructura para tu caso

```plaintext
domain/
├── models/
│   ├── aggregates/
│   │   └── Contribution.java
│   ├── commands/
│   │   ├── CreateContributionCommand.java
│   │   └── UpdateContributionCommand.java
│   ├── queries/
│   │   ├── GetContributionDetailsQuery.java
│   │   └── ListContributionsQuery.java
│   └── valueobjects/
│       ├── ContributionAmountVO.java
│       └── MemberIdVO.java
└── services/
    ├── ContributionCommandService.java           ← Interface
    ├── ContributionQueryService.java             ← Interface
    ├── MemberContributionCommandService.java     ← Interface
    └── MemberContributionQueryService.java       ← Interface
```

---

## Ejemplo de archivos

```java
// Contribution.java (Aggregate root)
public class Contribution {
    private final ContributionIdVO id;
    private ContributionAmountVO amount;
    private MemberIdVO memberId;
    // lógica de negocio...

    public void updateAmount(ContributionAmountVO newAmount) {
        // validaciones y reglas del dominio...
        this.amount = newAmount;
    }
}
```

```java
// CreateContributionCommand.java (record, solo datos)
public record CreateContributionCommand(BigDecimal amount, Long memberId) {}
```

```java
// ContributionCommandService.java (interface)
public interface ContributionCommandService {
    Long createContribution(BigDecimal amount, Long memberId);
}
```

---

## Reglas clave para la capa domain

- Todos los **Commands** y **Queries** deben ser records si solo transportan datos.
- Los **Value Objects** deben ser inmutables y tener validación interna.
- Los **Aggregates** concentran la lógica de negocio, validación e identidad.
- Los **Services** solo son interfaces, nunca implementación aquí.
- No uses nada de frameworks, annotations ni detalles de infraestructura.

---

## Resumen para IA y humanos

1. Mantén la **lógica de negocio pura** y las reglas de validación aquí.
2. Commands, Queries y ValueObjects siempre como records o clases inmutables.
3. Un **aggregate root** por carpeta, con subcarpetas sólo si el dominio es complejo.
4. Los servicios de dominio son sólo **interfaces**, los contratos de la lógica que luego implementa application/infrastructure.
5. La nomenclatura siempre debe ser clara: `[Acción][Entidad]Command`, `[Acción][Entidad]Query`, `[Entidad]Service`, `[NombreVO]`, `[Entidad]`.

---
