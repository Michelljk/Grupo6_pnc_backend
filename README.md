# Grupo 6 - Backend: Sistema de Organización de Torneos de Videojuegos

 ## 1. 📝 Descripción del Proyecto

Este proyecto es el componente backend de un sistema integral para la organización y gestión de torneos de videojuegos. Desarrollado con Spring Boot, proporciona una API RESTful robusta para manejar la creación de torneos, la inscripción de participantes, la generación y avance de brackets, la gestión de pagos a través de Stripe, la autenticación y autorización de usuarios mediante JWT, y la exportación de brackets a formato PDF. El sistema está diseñado para ser escalable y modular, utilizando patrones de diseño que facilitan la incorporación de nuevos formatos de torneo y funcionalidades.

## 2. 🎯 Objetivos del Proyecto

Los principales objetivos de este proyecto son:

*   **Facilitar la Creación y Gestión de Torneos** 🏆: Permitir a los administradores crear, configurar y gestionar torneos de diversos formatos (eliminatoria simple, doble, liga, suizo, round robin).
*   **Gestión de Participantes** 👥: Habilitar la inscripción y desinscripción de usuarios en torneos, con validaciones de elegibilidad (ELO mínimo/máximo).
*   **Generación y Avance de Brackets** 📊: Implementar la lógica para generar brackets iniciales y avanzar las rondas de los torneos de forma automatizada, según el formato seleccionado.
*   **Sistema de Pagos Integrado** 💳: Incorporar un sistema de pagos seguro a través de Stripe para manejar inscripciones premium y la compra de créditos virtuales.
*   **Autenticación y Autorización Segura** 🔒: Proteger la API mediante tokens JWT, garantizando que solo usuarios autenticados y autorizados puedan acceder a las funcionalidades correspondientes.
*   **Exportación de Datos** 📄: Ofrecer la capacidad de exportar la información de los brackets a formatos legibles como PDF.
*   **Estadísticas y Reportes** 📈: Generar estadísticas y reportes sobre el rendimiento de los jugadores y el progreso de los torneos.

## 3. 🧑‍💻 Integrantes del Equipo

Los siguientes colaboradores han contribuido al desarrollo de este proyecto:

*   **DAMD8** `<Daniel Alejandro Melendez Delgado>`
*   **SrGaray** `<Samuel Abraham Rodriguez Garay>`
*   **Michelljk** `<Diana Michell Velasquez Melendez>`


## 4. 🛠️ Tecnologías Utilizadas

El proyecto está construido sobre las siguientes tecnologías y librerías clave:

<p align="left">
  <img src="https://img.shields.io/badge/Spring_Boot-3.2.5-6DB33F?style=for-the-badge&logo=springboot&logoColor=white" alt="Spring Boot" />
  <img src="https://img.shields.io/badge/Java-21-007396?style=for-the-badge&logo=java&logoColor=white" alt="Java" />
  <img src="https://img.shields.io/badge/PostgreSQL-runtime-336791?style=for-the-badge&logo=postgresql&logoColor=white" alt="PostgreSQL" />
  <img src="https://img.shields.io/badge/Spring_Data_JPA-integrated-6DB33F?style=for-the-badge&logo=spring&logoColor=white" alt="Spring Data JPA" />
  <img src="https://img.shields.io/badge/Spring_Security-integrated-6DB33F?style=for-the-badge&logo=springsecurity&logoColor=white" alt="Spring Security" />
  <img src="https://img.shields.io/badge/JJWT-0.11.5-black?style=for-the-badge&logo=jsonwebtokens&logoColor=white" alt="JJWT" />
  <img src="https://img.shields.io/badge/BCrypt-integrated-black?style=for-the-badge&logo=springsecurity&logoColor=white" alt="BCryptPasswordEncoder" />
  <img src="https://img.shields.io/badge/Stripe-25.3.0-626CD9?style=for-the-badge&logo=stripe&logoColor=white" alt="Stripe Java" />
  <img src="https://img.shields.io/badge/MapStruct-1.5.5.Final-orange?style=for-the-badge&logo=mapstruct&logoColor=white" alt="MapStruct" />
  <img src="https://img.shields.io/badge/Lombok-integrated-red?style=for-the-badge&logo=lombok&logoColor=white" alt="Lombok" />
  <img src="https://img.shields.io/badge/iText7_Core-8.0.0-blue?style=for-the-badge&logo=itext&logoColor=white" alt="iText7 Core" />
  <img src="https://img.shields.io/badge/Apache_Maven-integrated-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white" alt="Apache Maven" />
</p>

## 5. 🏛️ Arquitectura Seleccionada

El proyecto sigue una arquitectura de N-Capas implementada con Spring Boot, adhiriéndose a principios de diseño limpio y separación de responsabilidades. A continuación, se detallan los componentes clave:

### 5.1. Capas de la Aplicación

*   **Capa de Presentación (Controladores)** 🌐: Expone la API RESTful. Los controladores (`@RestController`) manejan las solicitudes HTTP, validan los DTOs de entrada y delegan la lógica de negocio a la capa de servicios. Utiliza `@PreAuthorize` para la seguridad a nivel de método.
*   **Capa de Servicio (Lógica de Negocio)** ⚙️: Contiene la lógica central de la aplicación. Los servicios (`@Service`) orquestan las operaciones, interactúan con la capa de persistencia y aplican las reglas de negocio. Aquí se implementan patrones como Strategy para la flexibilidad en los formatos de torneo.
*   **Capa de Persistencia (Repositorios y Entidades)** 🗄️: Define las entidades del dominio (`@Entity`) y las interfaces de repositorio (`JpaRepository`) para interactuar con la base de datos PostgreSQL. Utiliza Spring Data JPA para simplificar las operaciones CRUD.
*   **Capa de Seguridad** 🛡️: Implementada con Spring Security, gestiona la autenticación (JWT) y la autorización (basada en roles y a nivel de método). Define filtros (`JwtAuthFilter`) para procesar los tokens JWT en cada solicitud.

### 5.2. Patrones de Diseño Destacados

*   **Strategy Pattern** 🧩: Utilizado en la gestión de torneos para permitir diferentes formatos (Eliminatoria Simple, Eliminatoria Doble, Liga, Suizo, Round Robin) de manera intercambiable. La interfaz `FormatoStrategy` define el contrato, y cada clase de estrategia implementa la lógica específica para su formato.
*   **DTO** 📦: Se utilizan DTOs para la comunicación entre las capas de presentación y servicio, asegurando una clara separación de la representación de datos y las entidades del dominio.

### 5.3. Flujo de Seguridad (JWT)

1.  El usuario envía credenciales a `/api/auth/login`.
2.  `AuthService` autentica al usuario y genera un JWT.
3.  En solicitudes subsiguientes, el JWT se envía en el encabezado `Authorization`.
4.  `JwtAuthFilter` intercepta la solicitud, valida el JWT y establece el contexto de seguridad de Spring.
5.  `@PreAuthorize` en los controladores y servicios verifica los roles del usuario antes de permitir el acceso a los recursos.

### 5.4. Integración con Stripe

El sistema se integra con Stripe para procesar pagos. Esto incluye:

*   Creación de intentos de pago para inscripciones a torneos o compra de créditos.
*   Manejo de webhooks de Stripe para actualizar el estado de los pagos de forma asíncrona y segura.

## 6. 📁 Estructura del Proyecto

La estructura principal del proyecto es la siguiente:

```
proyecto_pnc/
└── torneos/
     ├── documentacion/        # 📄 Colección de Postman, entorno y reporte PDF
    ├── src/
    │   ├── main/
    │   │   ├── java/com/example/torneos/
    │   │   │   ├── config/             # ⚙️ Configuraciones de la aplicación 
    │   │   │   ├── controller/         # 🌐 Controladores RESTful
    │   │   │   ├── dto/                # 📦 Objetos de Transferencia de Datos
    │   │   │   ├── entity/             # 🗄️ Entidades de la base de datos
    │   │   │   ├── enums/              # 🏷️ Enumeraciones del dominio
    │   │   │   ├── exception/          # ⚠️ Clases de excepción personalizadas
    │   │   │   ├── mapper/             # 🔄 Mapeadores de objetos 
    │   │   │   ├── repository/         # 💾 Interfaces de repositorio
    │   │   │   ├── security/           # 🔒 Componentes de seguridad 
    │   │   │   ├── service/            # 💼 Lógica de negocio y servicios
    │   │   │   │   └── strategy/       # 🧩 Implementaciones del patrón Strategy
    │   │   │   └── TorneosApplication.java # 🚀 Clase principal de la aplicación
    │   │   └── resources/            # 📄 Archivos de configuración y recursos estáticos
    │   │       ├── application.yaml  # 🔧 Configuración de la aplicación 
    │   │       ├── static/           # 🖼️ Recursos estáticos
    │   │       
    │   └── test/                   # 🧪 Pruebas unitarias e de integración
    ├── pom.xml                     # 🏗️ Archivo de configuración de Maven
    └── README.md                   # 📖 Este documento
```

## 7. 🚀 Instrucciones de Instalación, Configuración y Ejecución

Siga estos pasos para configurar y ejecutar el proyecto localmente.

### 7.1. Prerrequisitos

Asegúrese de tener instalados los siguientes componentes:

*   **Java Development Kit (JDK)**: Versión 21 o superior. ☕
*   **Apache Maven**: Versión 3.6.0 o superior. 🐘
*   **PostgreSQL**: Base de datos relacional. Puede instalarla localmente o usar un servicio en la nube. 🐘

### 7.2. Configuración de la Base de Datos

1.  **Crear Base de Datos**: Cree una base de datos PostgreSQL. Por ejemplo, `torneos_db`. ➕
2.  **Actualizar `application.yaml`**: Modifique el archivo `src/main/resources/application.yaml` con las credenciales de su base de datos:

    ```yaml
    spring:
      datasource:
        url: jdbc:postgresql://[HOST]:[PUERTO]/[NOMBRE_DB]
        username: [USUARIO_DB]
        password: [CONTRASEÑA_DB]
    ```

    *Reemplace `[HOST]`, `[PUERTO]`, `[NOMBRE_DB]`, `[USUARIO_DB]` y `[CONTRASEÑA_DB]` con sus valores.* El proyecto utiliza `ddl-auto: update`, por lo que las tablas se crearán o actualizarán automáticamente al iniciar la aplicación. 🔄

### 7.3. Configuración de Stripe

1.  **Obtener Claves de API**: Regístrese en [Stripe](https://stripe.com/) y obtenga su clave secreta de API y el secreto de firma del webhook. 🔑
2.  **Actualizar `application.yaml`**: Añada o modifique las siguientes propiedades en `src/main/resources/application.yaml`:

    ```yaml
    stripe:
      api-key: sk_test_YOUR_STRIPE_SECRET_KEY
      webhook-secret: whsec_YOUR_STRIPE_WEBHOOK_SECRET
    ```

    *Reemplace `YOUR_STRIPE_SECRET_KEY` y `YOUR_STRIPE_WEBHOOK_SECRET` con sus claves reales.* Para desarrollo local, puede usar herramientas como `ngrok` para exponer su webhook a Stripe. 🔗

### 7.4. Configuración de JWT

El secreto de JWT se encuentra en `src/main/resources/application.yaml`:

```yaml
jwt:
  secret: YOUR_SUPER_SECRET_JWT_KEY
  expiration: 8600000 # Tiempo de expiración en milisegundos
```

*Asegúrese de usar una clave secreta fuerte y única para producción.* La clave actual es solo un ejemplo. 🔐

### 7.5. Compilación del Proyecto

Navegue hasta el directorio raíz del proyecto (`torneos/`) y compile el proyecto usando Maven:

```bash
mvn clean install
```

Esto descargará las dependencias, compilará el código y generará el archivo JAR ejecutable en el directorio `target/`. ⚙️

### 7.6. Ejecución del Proyecto

Una vez compilado, puede ejecutar la aplicación de las siguientes maneras:

1.  **Desde el JAR ejecutable**:

    ```bash
    java -jar target/torneos-0.0.1-SNAPSHOT.jar
    ```

2.  **Usando Maven Spring Boot Plugin**:

    ```bash
    mvn spring-boot:run
    ```

La aplicación se iniciará en el puerto `8080` por defecto o el que usted configure `application.yaml` ▶️

## 8. 🔗 Endpoints Principales de la API

A continuación, se listan algunos de los endpoints clave disponibles en la API:

| Método | Ruta                      | Descripción                                        | Autenticación Requerida | Roles Requeridos |
| :----- | :------------------------ | :------------------------------------------------- | :---------------------- | :--------------- |
| `POST` | `/api/auth/register`      | Registra un nuevo usuario.                         | No                      | N/A              |
| `POST` | `/api/auth/login`         | Autentica un usuario y devuelve un JWT.            | No                      | N/A              |
| `POST` | `/api/torneos`            | Crea un nuevo torneo.                              | Sí                      | `ADMINISTRADOR`  |
| `GET`  | `/api/torneos`            | Lista todos los torneos disponibles.               | Sí                      | Cualquier        |
| `GET`  | `/api/torneos/{id}`       | Obtiene los detalles de un torneo por ID.          | Sí                      | Cualquier        |
| `PUT`  | `/api/torneos/{id}`       | Actualiza un torneo existente.                     | Sí                      | `ADMINISTRADOR`  |
| `DELETE`| `/api/torneos/{id}`       | Elimina un torneo.                                 | Sí                      | `ADMINISTRADOR`  |
| `POST` | `/api/torneos/{id}/inscribir/{usuarioId}` | Inscribe un usuario en un torneo.                  | Sí                      | Cualquier        |
| `DELETE`| `/api/torneos/{id}/remover/{usuarioId}` | Remueve un usuario de un torneo.                   | Sí                      | Cualquier        |
| `POST` | `/api/pagos/create-payment-intent` | Crea un intento de pago con Stripe.                | Sí                      | Cualquier        |
| `POST` | `/api/pagos/webhook`      | Endpoint para recibir webhooks de Stripe.          | No                      | N/A              |
| `GET`  | `/api/exportacion/bracket/{torneoId}/pdf` | Exporta el bracket de un torneo a PDF.             | Sí                      | Cualquier        |

---

**Autor**: Grupo 6 
