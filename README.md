# Incident Platform

A production-grade incident management backend system inspired by enterprise developer platforms. Automatically generates contextual incident summaries using RAG (Retrieval-Augmented Generation) with historical incident data.

## Overview

This platform provides a complete incident lifecycle management system with intelligent automation:
- Ingest alerts from monitoring systems
- Correlate alerts into incidents
- Generate AI-powered summaries using similar past incidents as context
- Route notifications based on severity
- Track incident resolution with full audit trail

## Architecture
| Stage | Description | Status |
|-------|-------------|--------|
| 1     | REST API + PostgreSQL + JPA | 🔄 In progress |
| 2     | Redis caching + idempotency | ⬜ Planned |
| 3     | Kafka async pipeline | ⬜ Planned |
| 4     | pgvector RAG + LLM summarization | ⬜ Planned |
| 5     | Agentic orchestration | ⬜ Planned |

## Tech Stack

- **Framework:** Spring Boot 3.x (Java 21)
- **Database:** PostgreSQL 16 with pgvector extension
- **Caching:** Redis 7
- **Messaging:** Apache Kafka (KRaft mode)
- **Vector Search:** pgvector for similarity search
- **Embeddings:** OpenAI API or Ollama (profile-based)
- **Containerization:** Docker Compose
- **API Docs:** SpringDoc OpenAPI (Swagger UI)
- **Build:** Maven

## Quick Start

### Prerequisites

- Java 21+
- Docker & Docker Compose
- Maven 3.8+

### Local Development

```bash
# Start infrastructure (PostgreSQL, Redis, Kafka)
docker-compose up -d

# Build the application
mvn clean install

# Run the application
mvn spring-boot:run

# Access Swagger UI
open http://localhost:8080/swagger-ui.html
```

## API Endpoints

### Incidents

```
POST   /api/v1/incidents              Create new incident
GET    /api/v1/incidents              List incidents (filterable by status, severity)
GET    /api/v1/incidents/{id}         Get incident details
PATCH  /api/v1/incidents/{id}/status  Update status (validated transitions)
PATCH  /api/v1/incidents/{id}/assign  Assign to user
GET    /api/v1/incidents/{id}/alerts  List incident alerts
```

### Alerts

```
POST   /api/v1/alerts                 Ingest alert (auto-correlates to incident)
```

### Users

```
POST   /api/v1/users                  Create user
GET    /api/v1/users/{id}             Get user details
```

## Data Flow

```
Monitoring System
    │
    ├─ POST /api/v1/alerts
    │       │
    │       └─ AlertService correlates → Incident (new or existing)
    │               │
    │               └─ [Stage 3] Kafka event → incidents.created
    │                       │
    │                       └─ [Stage 6] Agent Pipeline
    │                               ├─ EnrichmentAgent (gather context)
    │                               ├─ SummaryAgent (RAG + LLM)
    │                               │    ├─ Embed incident text
    │                               │    ├─ Find similar incidents (pgvector)
    │                               │    └─ Generate summary via LLM
    │                               └─ NotificationAgent (route by severity)
```

## Project Structure

```
src/main/java/com/example/incidentplatform/
├── controller/          # REST endpoints
├── service/             # Business logic
├── repository/          # Data access
├── domain/              # JPA entities
├── dto/                 # API contracts
├── exception/           # Custom exceptions
├── config/              # Spring configuration
└── [future stages]
    ├── event/           # Domain events (Stage 3)
    ├── messaging/       # Kafka producers/consumers (Stage 3)
    ├── rag/             # Embeddings & LLM clients (Stage 4)
    └── agent/           # Agent orchestration (Stage 6)
```

## License

MIT
