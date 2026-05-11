---
trigger: always_on
---

# Capa interfaces

## ¿Para qué sirve la capa interfaces?

La capa **interfaces** es el punto de entrada y salida de la aplicación. Se encarga de exponer la funcionalidad de tu dominio y tu aplicación a través de distintos mecanismos de entrada/salida: HTTP (REST), mensajería, CLI, etc.

### Funciones principales:
- Exponer endpoints REST (controllers)
- Recibir y deserializar requests, devolver responses
- Validar datos de entrada (a nivel superficial, no negocio)
- Transformar los datos entre los recursos de entrada/salida (resources) y comandos/queries/domain models
- Delegar la ejecución de lógica de negocio a la capa application

> **NO** debe poseer lógica de negocio ni lógica tecnológica concreta: solo “orquestar” el ingreso y salida de información.

---

## Estructura típica en tu código

Según lo que se observa en tu repo Contributions:

```
interfaces/
└── rest/
    ├── ContributionsController.java
    ├── MemberContributionsController.java
    ├── resources/
    └── transform/
```
Donde:
- Los controllers reciben solicitudes y gestionan la interacción con la capa application.
- resources y transform modelan los datos de entrada/salida y sus conversiones.

---

### Subcarpetas/tipos comunes

1. **rest/**
   - Controladores REST de cada agregado o sección funcional.
   - Ejemplo: `ContributionsController.java`, `MemberContributionsController.java`
2. **resources/**
   - Clases “RequestResource” y “ResponseResource” que definen la estructura de datos de entrada/salida de la API.
3. **transform/**
   - Mappers/Transformers (pueden llamarse también Assemblers) para convertir entre recursos API y modelos/domain/commands.

---

## Nomenclatura de archivos

### 1. Controllers

- `[Entidad]Controller.java`
  - Ejemplo: `ContributionsController.java`
  - Contienen métodos anotados con `@RestController` o `@Controller`.
  - Métodos expuestos suelen llamarse igual que la función a exponer: `createContribution`, `listContributions`, etc.
- Si tienes muchos endpoints segmenta por agregado: `MemberContributionsController.java`

### 2. Resources

- `[Accion][Entidad][Request|Response]Resource.java`
  - Ejemplo: `CreateContributionRequestResource.java`, `ContributionDetailResponseResource.java`
  - Siempre como record (Java moderno).
  - Representan el JSON que consume/produce tu API.
  - Si solo hay una operación, puedes usar `[Entidad][Request|Response]Resource.java`

### 3. Transform

- `[Entidad]ResourceMapper.java` o `[Entidad]ResourceTransformer.java`
  - Ejemplo: `ContributionResourceMapper.java`
  - Métodos principales: `toResource(model)`, `toDomain(resource)`, etc.
  - Clase de métodos estáticos o bien anotada con `@Component`.

---

## Ejemplo de estructura

```plaintext
interfaces/
└── rest/
    ├── ContributionsController.java
    ├── MemberContributionsController.java
    ├── resources/
    │   ├── CreateContributionRequestResource.java
    │   ├── ContributionDetailResponseResource.java
    └── transform/
        └── ContributionResourceMapper.java
```

---

## Ejemplo de archivos

```java
// ContributionsController.java
@RestController
@RequestMapping("/api/contributions")
public class ContributionsController {
    private final CreateContributionCommandService service;

    @PostMapping
    public ResponseEntity<ContributionDetailResponseResource> create(@RequestBody CreateContributionRequestResource req) {
        // Validación simple, mapeo y delegación
        var command = ContributionResourceMapper.toCommand(req);
        var result = service.handle(command);
        return ResponseEntity.ok(ContributionResourceMapper.toResource(result));
    }
}

// CreateContributionRequestResource.java
public record CreateContributionRequestResource(BigDecimal amount, Long memberId) {}

// ContributionDetailResponseResource.java
public record ContributionDetailResponseResource(Long contributionId, BigDecimal amount, String createdAt) {}

// ContributionResourceMapper.java
public class ContributionResourceMapper {
    public static CreateContributionCommand toCommand(CreateContributionRequestResource resource) { ... }
    public static ContributionDetailResponseResource toResource(Contribution domain) { ... }
}
```

---

## Reglas de nomenclatura y buenas prácticas

1. Los controladores siempre terminan en `Controller`.
2. Los resources siempre terminan en `RequestResource` y `ResponseResource`, son records.
3. Los mappers/transformers siempre terminan en `ResourceMapper` o `ResourceTransformer`.
4. Cada agregate funcional tiene su propio controller y carpeta de recursos si el dominio es grande.
5. La capa solo debe transformar datos y delegar, jamás lógica de negocio.

