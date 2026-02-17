# Proyecto API REST en Spring Boot

## Descripción General
Este proyecto consiste en una API REST desarrollada en Spring Boot (Java 21) que permite realizar cálculos dinámicos con un porcentaje adicional, almacenar estos cálculos en caché y llevar un historial de las operaciones realizadas.

## Características
- Microservicio basado en Kafka.
- API REST en Spring Boot (Java 21).
- Cálculo con porcentaje dinámico.
- Caché del porcentaje durante 30 minutos.
- Historial de llamadas asíncrono.
- Configuración de Feign para llamadas a APIs externas.
- Integración con H2 como base de datos en memoria para pruebas.
- Base de datos PostgreSQL para almacenar el historial de llamadas.

## Funcionalidades Principales
1. **Cálculo Dinámico**:
   - Endpoint: `/api/calculo-dinamico/calcular`
   - Método: `GET`
   - Parámetros: `num1`, `num2`
   - Respuesta: Devuelve el resultado de `num1 + num2 + porcentaje`.

2. **Historial de Llamadas**:
   - Endpoint: `/api/calculo-dinamico/historial`
   - Método: `GET`
   - Respuesta: Devuelve el historial de cálculos realizados.

## Caché
El porcentaje obtenido se almacena en memoria durante 30 minutos. Si el servicio externo falla, se utiliza el último valor almacenado.

## Instrucciones para Ejecutar el Proyecto en formato
1. **Construir la imagen**:
   ```bash
   docker-compose build
   ```
2. **Ejecutar los contenedores**:
   ```bash
   docker-compose up
   ```

## Ejemplos de Comandos `curl`
1. **Cálculo Dinámico**:
   ```bash
   curl -X GET "http://localhost:8085/api/calculo-dinamico/calcular?num1=10&num2=20"
   ```

2. **Historial de Llamadas**:
   ```bash
   curl -X GET "http://localhost:8085/api/calculo-dinamico/historial"
   ```
   
## PRUEBAS LOCALES en Projecto Tipo MAVEN con las siguientes configuraciones:
    # Desarrollo normal (H2 + Kafka mock)
    mvn spring-boot:run -Dspring-boot.run.profiles=dev
    
    # Desarrollo con Kafka local (H2 + Kafka real)
    mvn spring-boot:run -Dspring-boot.run.profiles=local-kafka

## Documentación
- Se utiliza Swagger para la documentación de la API.
  SI ejecuta el projecto en mvn local, Podra apuntar a esta url = http://localhost:8085/swagger-ui/index.html#/Calculos/calcular
  Para realizar pruebas en swagger


## Pruebas
Se implementan pruebas unitarias utilizando JUnit y Mockito para garantizar la funcionalidad del cálculo y el manejo del caché.
No sea realizaron todos los test. Solo implemente algunos para que puedan reconocer el el formato de testeo mio 

## Inversión de Control (IoC) y Dependencia de Inyección (DI)
El proyecto utiliza IoC y DI para gestionar las dependencias entre los componentes. Esto permite una mayor flexibilidad y facilita las pruebas unitarias.

## Principios SOLID
Se han aplicado los principios SOLID en el diseño del código para asegurar que sea mantenible y escalable:

## Conexión con la Base de Datos
El proyecto está configurado para conectarse a una base de datos PostgreSQL utilizando JPA (Java Persistence API) para almacenar el historial de llamadas.

## Docker Compose
Para crear la imagen del proyecto utilizando Docker Compose, se debe ejecutar el siguiente comando:
```bash
docker-compose down  # Limpia cualquier contenedor previo
docker-compose build --no-cache  # Reconstruye sin usar caché
docker-compose up  # Inicia los servicios
```
Esto construirá la imagen y levantará los servicios definidos en el archivo `docker-compose.yml`.

## Dependencias
El proyecto incluye las siguientes dependencias:
- Redis para almacenamiento en caché.
- JPA para la gestión de la base de datos PostgreSQL.

## Dependencias
El proyecto incluye las siguientes dependencias:
- **Spring Boot Starters**:
  - `spring-boot-starter-web`
  - `spring-boot-starter-data-jpa`
- **PostgreSQL**:
  - `postgresql` (runtime)
- **H2 Database** (para pruebas locales):
  - `h2` (runtime)
- **Kafka**:
  - `spring-kafka`
- **Redis**:
  - `spring-boot-starter-data-redis`
  - `jedis` (versión 5.1.0)
- **Cache**:
  - `spring-boot-starter-cache`
- **Feign Client**:
  - `spring-cloud-starter-openfeign` (versión 4.1.1)
- **Lombok**:
  - `lombok` (provided)
- **SpringDoc OpenAPI (Swagger)**:
  - `springdoc-openapi-starter-webmvc-ui` (versión 2.3.0)
- **Dependencias de Prueba**:
  - `spring-boot-starter-test` (test)
  - `spring-kafka-test` (test)

- Instrucciones claras para ejecutar el servicio están incluidas en este README.
