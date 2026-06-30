# Server Monitor

A full-stack server health monitoring dashboard with real-time status tracking and LINE alert notifications.

🔗 **Live Demo:** [server-monitor-mg-dotcom.vercel.app](https://server-monitor-mg-dotcom.vercel.app)

### Demo Access
| Role | Username | Password |
|---|---|---|
| Admin | demo_admin | demo1234 |
| Operator | demo_operator | demo1234 |

> **Admin** — full access: add/edit/remove servers, assign operators
> **Operator** — view only: restricted permissions to demonstrate Role-Based Access Control (RBAC)

⚠️ **Note:** To prevent spam, the LINE push notification feature is disabled for public demo accounts. Please check the **LINE Alert Preview** section below to see the real-time notification in action!

---

## Features

- **Auto Health Check** — polls registered endpoints every 30s via scheduled job
- **Status Tracking** — logs UP / DOWN / UNKNOWN per server, alerts only on status change
- **LINE Alerts** — uses targeted Push Messages to notify assigned operators when a server goes DOWN or recovers
- **Role-based Access** — ADMIN manages servers & operators, OPERATOR receives alerts and has view-only access
- **Operator Assignment** — assign multiple operators per server, triggers alert on assign if server is DOWN

---

## Tech Stack

| Layer | Tech |
|---|---|
| Frontend | Next.js 15, TypeScript, Tailwind CSS |
| Backend | Java Spring Boot, Spring Security (JWT) |
| Database | PostgreSQL |
| Scheduler | Spring `@Scheduled` |
| Notification | LINE Messaging API |
| Deployment | Vercel (FE), Railway (BE), Supabase (DB) |

---

## Architecture

```text
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

## ER Diagram

<img width="2270" height="1270" alt="supabase-schema-hrkgvhzpbyyrzeyaiiaq" src="https://github.com/user-attachments/assets/2ac7a441-fef0-40cc-b3d1-99b0519c044e" />

---

## LINE Alert Preview
Targeted push notifications (using specific LINE User IDs) sent exclusively to assigned operators when a server goes DOWN or recovers. No spammy broadcasts.

https://github.com/user-attachments/assets/42a8cbd3-a3e3-4c8e-b644-afb287d11120
