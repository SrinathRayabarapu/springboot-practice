# spring-boot-redis-caching

Distributed caching patterns module for the Spring Boot practice multi-module project.

Demonstrates Redis-backed caching with Lettuce, Spring Cache, and Redisson distributed locks, using profile-driven database configuration suitable for local development and cloud deployments.

## Patterns

| Pattern | Use case | Implementation |
|---|---|---|
| **Cache-Aside** | Read-heavy product catalog | `@Cacheable` / `@CachePut` / `@CacheEvict` on cache `products` |
| **Write-Through** | Inventory stock consistency | JPA + Redis update inside the same `@Transactional` boundary |
| **Write-Behind** | High-frequency page-view analytics | Redis List (`opsForList`) + `@Scheduled` batch drain to DB |
| **Cache Stampede** | Flash-sale item access | Redisson `RLock` ensures a single DB load under contention |

## Stack

- Spring Boot `2.7.0` / Java `17`
- `spring-boot-starter-data-redis` (Lettuce) + `spring-boot-starter-cache`
- `redisson-spring-boot-starter` `3.17.4`
- H2 in-memory (dev) / PostgreSQL-ready (prod)
- Redis `7.0-alpine` + Redis Commander via Docker Compose

## 1. Start Redis (and Redis Commander)

From the module `docker/` directory:

```bash
cd spring-boot-redis-caching/docker
docker compose up -d
```

Verify containers:

```bash
docker compose ps
```

| Service | URL / Port |
|---|---|
| Redis | `localhost:6379` |
| Redis Commander UI | http://localhost:8081 |

Stop later with:

```bash
docker compose down
```

## 2. Run the application (dev profile)

```bash
# from repository root
mvn -pl spring-boot-redis-caching -am spring-boot:run
```

Defaults:

- Active profile: `dev` (`spring.profiles.active=dev`)
- App: http://localhost:8085
- Database: H2 `jdbc:h2:mem:cachedb` (console at http://localhost:8085/h2-console)
- Redis: `localhost:6379`

## 3. REST API base path

All pattern endpoints are under:

```text
http://localhost:8085/api/v1/cache/...
```

| Pattern | Base path |
|---|---|
| Cache-Aside | `/api/v1/cache/aside/...` |
| Write-Through | `/api/v1/cache/write-through/...` |
| Write-Behind | `/api/v1/cache/write-behind/...` |
| Stampede | `/api/v1/cache/stampede/...` |

## 4. cURL examples for each pattern

### 4.1 Cache-Aside (product catalog)

Create a product (DB write + `@CachePut`):

```bash
curl -s -X POST http://localhost:8085/api/v1/cache/aside/products \
  -H 'Content-Type: application/json' \
  -d '{"sku":"SKU-100","name":"Keyboard","price":49.99,"stock":25}'
```

First read = cache miss → DB load → automatic cache population (`@Cacheable`):

```bash
curl -s http://localhost:8085/api/v1/cache/aside/products/1
```

Second read = cache hit (same URL). Watch application logs for miss vs silent hit.

Update (refreshes cache via `@CachePut`):

```bash
curl -s -X PUT http://localhost:8085/api/v1/cache/aside/products/1 \
  -H 'Content-Type: application/json' \
  -d '{"sku":"SKU-100","name":"Keyboard Pro","price":59.99,"stock":20}'
```

Delete (evicts cache via `@CacheEvict`):

```bash
curl -s -o /dev/null -w "%{http_code}\n" -X DELETE http://localhost:8085/api/v1/cache/aside/products/1
```

**Redis Commander:** look for Spring Cache keys under the `products` cache (typically prefixed like `products::1`). Inspect TTL on the key detail view.

### 4.2 Write-Through (inventory stock)

Seed inventory (creates DB row and Redis key `products:inventory:{id}` in one transaction):

```bash
curl -s -X POST http://localhost:8085/api/v1/cache/write-through/inventory \
  -H 'Content-Type: application/json' \
  -d '{"sku":"SKU-200","name":"Mouse","price":19.99,"stock":100}'
```

Read stock (cache-first; miss loads DB then writes through to Redis):

```bash
curl -s http://localhost:8085/api/v1/cache/write-through/inventory/1
```

Update stock synchronously in DB + Redis:

```bash
curl -s -X PUT http://localhost:8085/api/v1/cache/write-through/inventory/1/stock \
  -H 'Content-Type: application/json' \
  -d '{"stock":18}'
```

**Redis Commander:** open key `products:inventory:1`, confirm JSON payload and TTL after create/update.

### 4.3 Write-Behind (page-view batching)

Enqueue analytics events instantly onto Redis List `analytics:page-views`:

```bash
curl -s -X POST http://localhost:8085/api/v1/cache/write-behind/page-views \
  -H 'Content-Type: application/json' \
  -d '{"pagePath":"/products/1","visitorId":"v-1","sessionId":"s-1"}'

curl -s -X POST http://localhost:8085/api/v1/cache/write-behind/page-views \
  -H 'Content-Type: application/json' \
  -d '{"pagePath":"/products/2","visitorId":"v-2","sessionId":"s-2"}'
```

Inspect queue depth:

```bash
curl -s http://localhost:8085/api/v1/cache/write-behind/page-views/pending-count
```

Force an immediate drain (also runs automatically on a schedule):

```bash
curl -s -X POST http://localhost:8085/api/v1/cache/write-behind/page-views/flush
```

After flush, pending count should drop toward `0` and rows appear in H2 table `page_view_events`.

**Redis Commander:** open list key `analytics:page-views`. Before flush you should see list elements; after flush the list shrinks or empties.

### 4.4 Stampede locking (flash-sale)

Ensure a product exists (reuse cache-aside create if needed), then evict the flash-sale cache entry:

```bash
curl -s -o /dev/null -w "%{http_code}\n" \
  -X DELETE http://localhost:8085/api/v1/cache/stampede/flash-sale/1/cache

curl -s -X POST http://localhost:8085/api/v1/cache/stampede/flash-sale/db-load-count/reset
```

Simulate concurrent misses (only one DB load should be counted):

```bash
curl -s -X POST http://localhost:8085/api/v1/cache/stampede/flash-sale/db-load-count/reset

for i in 1 2 3 4 5; do
  curl -s http://localhost:8085/api/v1/cache/stampede/flash-sale/1 >/dev/null &
done
wait

curl -s http://localhost:8085/api/v1/cache/stampede/flash-sale/db-load-count
```

Expected: `"dbLoads": 1` (or a very small number), not one load per concurrent request.

**Redis Commander:** key `products:flash-sale:1` appears after the first successful miss; Redisson lock keys are short-lived under names like `lock:flash-sale:product:1`.

## 5. Observing keys, TTLs, and queues in Redis Commander

1. Open http://localhost:8081
2. Select the `local` Redis connection (configured by Docker Compose as `redis:6379`)
3. Browse keys:

| What to inspect | Key / pattern | Notes |
|---|---|---|
| Cache-Aside | `products::*` | Spring Cache entries; check **TTL** |
| Write-Through inventory | `products:inventory:*` | JSON product payload + TTL |
| Write-Behind queue | `analytics:page-views` | Redis **List**; length = pending events |
| Stampede flash-sale | `products:flash-sale:*` | Populated after locked DB load |
| Stampede locks | `lock:flash-sale:product:*` | Transient Redisson locks |

Tips:

- Click a key to view type (string/list), value, and remaining TTL.
- For lists, expand elements to see serialized `PageViewEvent` payloads.
- Re-run the cURL flows above and refresh Redis Commander to watch population, eviction, and queue drain.

## 6. Switching profiles: `dev` → `prod`

### Dev (default)

No extra env vars required when Redis runs locally via Docker Compose:

```bash
mvn -pl spring-boot-redis-caching -am spring-boot:run
# or explicitly:
SPRING_PROFILES_ACTIVE=dev mvn -pl spring-boot-redis-caching -am spring-boot:run
```

### Prod (cloud / external services)

Set standard Spring environment variables so ElastiCache / Azure Redis / RDS (or equivalents) work as drop-in replacements:

```bash
export SPRING_PROFILES_ACTIVE=prod
export SERVER_PORT=8085

export SPRING_DATASOURCE_URL=jdbc:postgresql://db-host:5432/caching
export SPRING_DATASOURCE_USERNAME=caching
export SPRING_DATASOURCE_PASSWORD=secret
export SPRING_DATASOURCE_DRIVER_CLASS_NAME=org.postgresql.Driver
export SPRING_JPA_HIBERNATE_DDL_AUTO=validate
export SPRING_JPA_DATABASE_PLATFORM=org.hibernate.dialect.PostgreSQLDialect

export SPRING_REDIS_HOST=redis-host
export SPRING_REDIS_PORT=6379
export SPRING_REDIS_PASSWORD=your-redis-password

mvn -pl spring-boot-redis-caching -am spring-boot:run
```

Optional caching tunables:

```bash
export CACHE_PRODUCT_TTL_SECONDS=600
export CACHE_WRITE_BEHIND_QUEUE_KEY=analytics:page-views
export CACHE_WRITE_BEHIND_FLUSH_DELAY_MS=2000
export CACHE_WRITE_BEHIND_BATCH_SIZE=50
```

## 7. Automated tests

```bash
mvn -pl spring-boot-redis-caching -am test
```

Unit tests mock Redis/Redisson and cover cache-aside delegation, write-through stock updates, write-behind list drain/batch insert, and stampede lock behavior.

## Module layout

```text
spring-boot-redis-caching/
├── docker/docker-compose.yml
├── README.md
├── pom.xml
└── src/main/java/com/srinath/caching/
    ├── config/          # RedisCacheManager, JSON serializers, properties
    ├── domain/
    ├── repository/
    ├── service/
    ├── controller/      # /api/v1/cache/...
    └── patterns/
        ├── cacheaside/
        ├── writethrough/
        ├── writebehind/
        └── stampede/
```
