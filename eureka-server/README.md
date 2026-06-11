# Eureka Server — Service Discovery

Central registry where all microservices announce themselves.
The API Gateway queries this to route requests without hardcoded URLs.

## Quick Start


### Run locally (Maven)
```bash
cd eureka-server
mvn spring-boot:run
```
Dashboard → http://localhost:8761  
Login: `admin` / `admin` (dev defaults)

### Run with Docker
```bash
docker build -t eureka-server .
docker run -p 8761:8761 eureka-server
```

### Run with Docker Compose
```bash
docker-compose up
```

## Ports

| Port | Purpose |
|------|---------|
| 8761 | Eureka dashboard + registration endpoint |

## Configuration

| Property | Default | Description |
|----------|---------|-------------|
| `eureka.server.enable-self-preservation` | `false` (dev) | Prevents eviction during network instability |
| `eureka.server.eviction-interval-timer-in-ms` | `5000` | How often to evict dead instances |
| `spring.security.user.name` | `admin` | Dashboard username |
| `spring.security.user.password` | `admin` | Dashboard password — change in prod |

## How Other Services Register

Add this to each microservice's `application.yml`:

```yaml
eureka:
  client:
    service-url:
      defaultZone: http://admin:admin@localhost:8761/eureka/
  instance:
    prefer-ip-address: true
```

## Production Checklist

- [ ] Set `EUREKA_USER` and `EUREKA_PASSWORD` via environment variables
- [ ] Enable self-preservation: `eureka.server.enable-self-preservation: true`
- [ ] Use `--spring.profiles.active=prod`
- [ ] Deploy behind a reverse proxy (nginx/ALB) with TLS
- [ ] Consider running 2+ Eureka peers for HA
