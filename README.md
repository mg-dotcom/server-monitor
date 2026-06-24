# Server Monitor

A full-stack server health monitoring dashboard with real-time status tracking and LINE alert notifications.

🔗 **Live Demo:** [server-monitor-mg-dotcom.vercel.app](https://server-monitor-mg-dotcom.vercel.app)

### Demo Access
| Role | Username | Password |
|---|---|---|
| Admin | demo_admin | demo1234 |
| Operator | demo_operator | demo1234 |

> **Admin** — full access: add/edit/remove servers, assign operators
> **Operator** — view only: receives LINE alerts when assigned server goes DOWN

---

## Features

- **Auto Health Check** — polls registered endpoints every 30s via scheduled job
- **Status Tracking** — logs UP / DOWN / UNKNOWN per server, alerts only on status change
- **LINE Alerts** — pushes alert to assigned operators when server goes DOWN or recovers
- **Role-based Access** — ADMIN manages servers & operators, OPERATOR receives alerts only
- **Operator Assignment** — assign multiple operators per server, alert on assign if server is DOWN

---

## Tech Stack

| Layer | Tech |
|---|---|
| Frontend | Next.js 15, TypeScript, Tailwind CSS |
| Backend | Java Spring Boot, Spring Security (JWT) |
| Database | PostgreSQL (Railway) |
| Scheduler | Spring `@Scheduled` |
| Notification | LINE Messaging API |
| Deployment | Vercel (FE), Railway (BE + DB) |

---

## Architecture

```
Next.js (Vercel)
    │
    ├── Server Actions → Spring Boot REST API (Railway)
    │                        │
    │                        ├── PostgreSQL
    │                        ├── @Scheduled health check (every 30s)
    │                        └── LINE Messaging API (on status change)
    │
    └── StatusPoller (client) → router.refresh() every 30s
```

---

## ER Diagram

<img width="2270" height="1270" alt="supabase-schema-hrkgvhzpbyyrzeyaiiaq" src="https://github.com/user-attachments/assets/2ac7a441-fef0-40cc-b3d1-99b0519c044e" />

```
