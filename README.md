# Shortify

A URL shortener built with Spring Boot 3.4 / Java 21, backed by MariaDB and Redis.

## How it works

- Users register or log in and receive a JWT (valid for 24 hours). Passwords are BCrypt-hashed; roles are `USER` and `ADMIN`.
- `POST /api/v1/shorten` (JWT with `USER` authority) stores a long URL with a TTL in minutes. You can supply a custom alphanumeric `alias`, or the service derives one from the next MariaDB sequence value encoded in Base62 (prefixed with `_`). The short URL is built from the incoming request's scheme, host, and port.
- `GET /{alias}` is public and returns `307 Temporary Redirect` with the target in the `Location` header, or `404` when the link is missing or expired. Redirect lookups are cached in Redis.
- `POST /api/v1/cleanup` (JWT with `USER` or `ADMIN` authority) asynchronously deletes expired URL rows from the database.
- Every request is rate-limited with a Redis-backed token bucket, keyed by username for authenticated callers and by client IP otherwise. Defaults: 5 requests, refilling 5 tokens every 10 seconds; responses include `X-RateLimit-*` headers and `429` when exhausted.
- JPA creates/updates tables at startup and `schema.sql` creates the ID sequence. The JWT secret, database, and Redis settings are development defaults in the source.

See [doc.md](DOC.md) for curl requests covering every endpoint.

## Run with Docker Compose

From the repository root:

```bash
docker compose up -d --build      # build the app image and start app + MariaDB + Redis
docker compose logs -f shortify-service
docker compose ps
```

The API is at `http://localhost:8080`; MariaDB (`3306`) and Redis (`6379`) are also published. On first start, wait for the database healthcheck to pass — the app waits for a healthy database before starting.

Stop or reset:

```bash
docker compose down               # stop containers, keep data in named volumes
docker compose down -v            # stop containers and delete database/Redis data
```

The root `docker-compose.yml` builds this repository's source using `Dockerfile`. A full-stack variant in `docker/docker-compose.yml` also builds the web UI (UI on port `80`, API on `8080`); it clones the `master` branches of the backend and UI repos and uses the same MariaDB healthcheck and Redis settings:

```bash
docker compose -f docker/docker-compose.yml up -d --build
```
