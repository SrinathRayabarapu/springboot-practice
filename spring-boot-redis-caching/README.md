# spring-boot-redis-caching

Distributed caching patterns module for the Spring Boot practice multi-module project.

Demonstrates Redis-backed caching with Lettuce, Redisson distributed locks, and profile-driven database configuration.

## Patterns

| Pattern | Behavior |
|---|---|
| **Cache-Aside** | Read cache first; on miss load DB and populate cache. Updates invalidate cache. |
| **Write-Through** | Every write updates DB and cache in the same request path. |
| **Write-Behind** | Writes update cache immediately and queue async DB persistence. |
| **Cache Stampede** | On miss, a Redisson lock ensures only one loader hits the DB. |

## Stack

- Spring Boot `2.7.0`
- Java `17`
- Spring Data Redis (Lettuce)
- Redisson `3.17.7` (distributed locks)
- H2 (dev) / PostgreSQL-ready (prod)
- Redis `7.0` via Docker Compose

## Quick start

### 1. Start Redis

```bash
cd spring-boot-redis-caching/docker
docker compose up -d
```

### 2. Run the app (dev profile)

```bash
cd ../..
mvn -pl spring-boot-redis-caching -am spring-boot:run
```

Default profile is `dev` (H2 + local Redis). App listens on port `8085`.

### 3. Sample API calls

```bash
# Create via cache-aside
curl -s -X POST http://localhost:8085/api/products/cache-aside \
  -H 'Content-Type: application/json' \
  -d '{"sku":"SKU-100","name":"Keyboard","price":49.99,"stock":25}'

# Read (cache miss then hit)
curl -s http://localhost:8085/api/products/cache-aside/1

# Write-through update
curl -s -X PUT http://localhost:8085/api/products/write-through/1 \
  -H 'Content-Type: application/json' \
  -d '{"sku":"SKU-100","name":"Keyboard Pro","price":59.99,"stock":20}'

# Write-behind create + pending count + flush
curl -s -X POST http://localhost:8085/api/products/write-behind \
  -H 'Content-Type: application/json' \
  -d '{"sku":"SKU-200","name":"Mouse","price":19.99,"stock":100}'
curl -s http://localhost:8085/api/products/write-behind/pending-count
curl -s -X POST http://localhost:8085/api/products/write-behind/flush

# Stampede-protected read
curl -s http://localhost:8085/api/products/stampede/1
```

## Profiles & configuration

| Profile | Database | Notes |
|---|---|---|
| `dev` (default) | H2 in-memory | Local Redis defaults (`localhost:6379`) |
| `prod` | External RDBMS | Requires `DB_*` and `REDIS_*` env vars |

Common environment variables:

```bash
SPRING_PROFILES_ACTIVE=prod
SERVER_PORT=8085

DB_URL=jdbc:postgresql://db-host:5432/caching
DB_USERNAME=caching
DB_PASSWORD=secret
DB_DRIVER=org.postgresql.Driver
JPA_DDL_AUTO=validate
JPA_DIALECT=org.hibernate.dialect.PostgreSQLDialect

REDIS_HOST=redis-host
REDIS_PORT=6379
REDIS_PASSWORD=
REDISSON_ADDRESS=redis://redis-host:6379

CACHE_PRODUCT_TTL_SECONDS=600
CACHE_WRITE_BEHIND_FLUSH_DELAY_MS=2000
```

## Tests

Unit tests mock Redis/Redisson and cover each pattern:

```bash
mvn -pl spring-boot-redis-caching -am test
```

## Module layout

```text
spring-boot-redis-caching/
├── docker/docker-compose.yml
├── src/main/java/com/example/caching/
│   ├── config/
│   ├── domain/
│   ├── repository/
│   ├── service/
│   ├── controller/
│   └── patterns/
│       ├── cacheaside/
│       ├── writethrough/
│       ├── writebehind/
│       └── stampede/
└── src/main/resources/
    ├── application.yml
    ├── application-dev.yml
    └── application-prod.yml
```
