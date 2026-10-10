# Product and design decisions

| Decision | Chosen direction | Alternatives considered | Reason |
|---|---|---|---|
| AI task creation | Suggestions require explicit per-item selection and confirmation | Auto-create on analysis; bulk one-tap save | Preserves trust and gives the user control over inferred data |
| Data ownership | Local database is source of truth; AI is an explicit remote assist | Cloud-first task store; continuous sync | Offline core use and limited initial privacy/backend scope |
| Primary navigation | Today, Inbox, Tasks, Focus, Settings | Put Planner/Insights at top level | Keeps primary actions reachable while secondary views remain nested |
| Daily planning | Ordered recommendation with estimates | Exact calendar time blocks | No availability is known unless user configures it |
| Deadlines | Optional date-only value in initial model | Mandatory date/time or inferred default | Avoids inventing precision and timezone errors |
| Focus timer | Persisted transitions and timestamp-derived elapsed time | UI counter coroutine | Resilient to backgrounding, process death and relaunch |
| Insights | Traceable counts and durations | Composite productivity score/streaks | Metrics should be explainable and avoid unsupported claims |
| Visual identity | Warm neutral canvas, pine primary, restrained semantic colors | Neon/gradient AI styling or generic dashboard cards | Supports calm scanning and a distinctive but practical product feel |
| Platform UI | Shared Compose Multiplatform product UI with native app entry points | Separate full native UI per platform | Appropriate code sharing while preserving platform lifecycle responsibilities |
| Authentication/sync | Excluded from initial scope | Add accounts and cloud backup immediately | No requirement established; reduces backend and privacy surface |
| Architecture | Pragmatic domain/data/presentation boundaries in existing shared module | Full micro-module Clean Architecture from day one | Starter is small; package boundaries first, module split only when justified |
| Dependency selection | No new dependency until version compatibility is verified | Add requested libraries at latest versions immediately | Existing Kotlin/AGP/CMP versions are specific and KMP compatibility cannot be assumed |

## Assumptions to revisit

- English is the initial product copy; locale-aware dates and time zones are still required.
- The first AI backend is not implemented or selected; client contract remains vendor-neutral.
- A 12,000-code-point note limit is a starting proposal and must be checked against backend limits and user testing.
- Analytics/telemetry are omitted unless privacy, consent, and a concrete product question justify them.
