---
trigger: always_on
---

# Capa infrastructure

## ¿Qué se hace en infrastructure?

- Implementa detalles tecnológicos del sistema (persistencia, clientes HTTP, configuración de beans, integraciones externas).
- Proporciona adaptadores concretos para los contratos definidos en dominio, como repositorios, servicios externos, gateways, etc.
- Traduce las abstracciones del dominio a implementaciones usando frameworks (por ejemplo, Spring Data JPA).

---

## Estructura en tu proyecto

Según tu código, el árbol principal es:

```
infrastructure/
├── configuration/
│   └── RestTemplateConfig.java
├── persistance/
│   └── jpa/
├── rest/
│   ├── BillClient.java
│   ├── HouseholdClient.java
│   └── IamClient.java
```

### Explicación y propósito de cada carpeta/archivo

#### 1. configuration/
- **Propósito:** Configuración de beans y componentes técnicos generales (por ejemplo, `RestTemplate`).
- **Archivos típicos:**
  - `RestTemplateConfig.java`: Configura el bean para consumo de APIs REST externas, muchas veces con anotación `@Configuration`.

#### 2. persistance/jpa/
- **Propósito:** Implementación de los repositorios de dominio usando JPA/Spring Data.
- **Archivos típicos:**
  - `[Entidad]Repository.java` que extienden de `JpaRepository`
  - Ejemplo:
    - `ContributionRepository.java` (extends JpaRepository<ContributionEntity, Long>)
  - Aquí es donde conectas la abstracción del dominio (`ContributionRepository` interface) con la base de datos a través de JPA.

#### 3. rest/
- **Propósito:** Clientes para consumir servicios REST externos.
- **Archivos típicos:**
  - `BillClient.java`, `HouseholdClient.java`, `IamClient.java`
  - Suelen usar `RestTemplate` o `WebClient` y muestran métodos para integración con otros microservicios.

---

## Ejemplo de nomenclatura

### En persistance/jpa

- `[Entidad]Repository.java`
  - Ejemplo: `ContributionRepository.java`
  - Extiende de `JpaRepository<ContributionEntity, Long>`
  - Métodos personalizados pueden añadirse aquí.

### En rest

- `[ServicioExterno]Client.java`
  - Ejemplo: `BillClient.java`, `IamClient.java`
  - Para cada microservicio o API externa se crea un `Client`.

### En configuration

- `[NombreFeature]Config.java`
  - Ejemplo: `RestTemplateConfig.java`
  - Siempre usar el sufijo `Config`.

---

## Ejemplo de archivos

```java
// ContributionRepository.java (infraestructura, persistance/jpa)
package com.fiscalliance.contributions.infrastructure.persistance.jpa;

import com.fiscalliance.contributions.infrastructure.persistance.jpa.entities.ContributionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContributionRepository extends JpaRepository<ContributionEntity, Long> {
    // Métodos personalizados si son necesarios
}
```

```java
// RestTemplateConfig.java (configuration)
package com.fiscalliance.contributions.infrastructure.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
```

```java
// BillClient.java (rest)
package com.fiscalliance.contributions.infrastructure.rest;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class BillClient {
    private final RestTemplate restTemplate;

    public BillClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
    // Métodos para consumo del microservicio Bills
}
```

---

## Reglas clave (para humanos e IA)

1. Los **repositorios** bajo `persistance/jpa` SIEMPRE extienden de `JpaRepository` y llevan el sufijo `Repository`.
2. Los **clientes REST** siempre terminan en `Client` y encapsulan llamadas HTTP a servicios externos.
3. Las **clases de configuración** terminan en `Config` y llevan la anotación `@Configuration`.
4. Ninguna clase aquí debe contener lógica de negocio, sólo de integración/tecnología.
5. Si hay mapeadores Entity <-> Domain, suelen ir en `persistance/jpa` o una subcarpeta `mapper/`.

---

## Estructura ejemplo

```plaintext
infrastructure/
├── configuration/
│   └── RestTemplateConfig.java
├── persistance/
│   └── jpa/
│       └── ContributionRepository.java
├── rest/
│   ├── BillClient.java
│   ├── HouseholdClient.java
│   └── IamClient.java
```
*(agrega más repositorios/clientes según vaya creciendo tu microservicio)*

---
