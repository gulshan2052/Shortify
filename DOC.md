# API requests (curl)

All examples assume the default Docker Compose setup with the API at `http://localhost:8080`. Replace `BASE` (and the token) if yours differs.

All bodies are JSON. Rate limiting (5 requests, refilled 5 per 10 seconds) applies to these calls too; a `429` response includes a `Retry-After` header.

```bash
BASE=http://localhost:8080
```

## 1. Register — `POST /api/v1/auth/register` (public)

```bash
curl -i -X POST "$BASE/api/v1/auth/register" \
  -H "Content-Type: application/json" \
  -d '{"firstname":"Ada","lastname":"Lovelace","email":"ada@example.com","password":"secret123","role":"USER"}'
```

`role` must be `USER` or `ADMIN` (uppercase). Returns `201` with `{"token":"...","message":"User registered successfully"}`; duplicate email or an invalid role returns `409`.

## 2. Log in — `POST /api/v1/auth/login` (public)

```bash
curl -i -X POST "$BASE/api/v1/auth/login" \
  -H "Content-Type: application/json" \
  -d '{"email":"ada@example.com","password":"secret123"}'
```

Returns `200` with `{"token":"...","message":"User logged in successfully"}`; bad credentials return `401`. Save the token for the protected endpoints (requires `jq`):

```bash
TOKEN=$(curl -s -X POST "$BASE/api/v1/auth/login" \
  -H "Content-Type: application/json" \
  -d '{"email":"ada@example.com","password":"secret123"}' | jq -r '.token')
```

## 3. Shorten a URL — `POST /api/v1/shorten` (JWT, `USER`)

`longUrl` and `ttlMinutes` are required; `alias` is optional.

Auto-generated alias:

```bash
curl -i -X POST "$BASE/api/v1/shorten" \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"longUrl":"https://spring.io/projects/spring-boot","ttlMinutes":1440}'
```

Custom alias (alphanumeric only, must be unused):

```bash
curl -i -X POST "$BASE/api/v1/shorten" \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"longUrl":"https://spring.io/guides","ttlMinutes":60,"alias":"docs"}'
```

Success returns `200` with `status: 1`, `shortUrl`, `alias`, `longUrl`, and `message`. Business errors also return HTTP `200` with a different `status`: `2` invalid URL, `3` alias already taken, `4` alias contains invalid characters.

## 4. Redirect — `GET /{alias}` (public)

Using the custom alias from above:

```bash
curl -i "$BASE/docs"
```

Returns `307 Temporary Redirect` with `Location: https://spring.io/guides`. To follow it:

```bash
curl -L "$BASE/docs"
```

A missing or expired link returns `404` with `Resource not found or has expired`.

## 5. Clean up expired links — `POST /api/v1/cleanup` (JWT, `USER` or `ADMIN`)

```bash
curl -i -X POST "$BASE/api/v1/cleanup" \
  -H "Authorization: Bearer $TOKEN"
```

Returns `200` with `Cleanup started`; deletion of expired rows runs asynchronously.
