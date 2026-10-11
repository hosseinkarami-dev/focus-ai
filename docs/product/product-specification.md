# FocusAI product specification

## Product summary

FocusAI is a local-first personal productivity app for turning rough notes into an intentional daily plan. Its distinctive workflow is a transparent review step: AI proposes structured work, the user edits and selects it, then explicitly confirms what becomes a task. Tasks, notes, and focus history remain useful without a network connection.

## Users and needs

The initial audience is freelancers, developers, students, and knowledge workers balancing commitments. They need quick capture, help finding the next actionable step, realistic workload visibility, and confidence that automation will not make commitments on their behalf.

## Product goals

- Capture thoughts with minimal friction and keep them locally available.
- Turn a note into editable, clearly marked suggestions through an optional AI service.
- Make a useful plan from priorities and estimates without implying calendar availability.
- Track completion and timestamp-based focus sessions, then explain simple activity metrics.
- Work offline for all core capture, task, planning, and history actions.

## MVP scope

1. Five primary destinations: Today, Inbox, Tasks, Focus, Settings.
2. Local note and task CRUD, completion, subtasks, basic search/filtering, and an ordered daily plan.
3. Note analysis and task breakdown behind a common provider interface; deterministic mock first and secure backend remote provider later.
4. Review/edit/select/confirm AI proposals, with uncertainty and inferred fields visibly disclosed.
5. A focus timer persisted by start/pause/resume/end timestamps, with optional task association.
6. Explainable insights derived from locally persisted completions and sessions.
7. Light, dark, and system theme selection; privacy and local-data controls.

## Exclusions for initial release

No accounts, subscription, multi-user sharing, cloud sync/backup, calendar integration, exact time-block scheduling, social features, streak pressure, or proprietary productivity score. No direct provider calls or credentials from the app. These can be reconsidered only when a validated user need and a data/privacy plan exist.

## Product rules

- AI suggestions never persist as tasks until the user confirms the selected proposals.
- An unknown date remains unknown. A relative date is resolved only with a supplied reference date and timezone, and remains labeled as interpreted until confirmed.
- Suggestion provenance is explicit until confirmation; invalid fields are omitted or flagged, never guessed into certainty.
- Local operations do not depend on the AI service. Network failure preserves the source note and any local edits.
- User-authored subtasks are never overwritten by task breakdown output.

## Success criteria

Product quality is evaluated with observable outcomes rather than a fabricated score:

- A new user can capture a note and find it again offline.
- A user can inspect, edit, reject, and selectively confirm proposed tasks.
- No AI response can write directly to local persistence.
- Today clearly shows the next action and total planned estimate, including empty and overdue cases.
- A focus session's duration is correct after backgrounding and process restoration within platform lifecycle limits.
- Every insight can be traced to stored task/session records and a documented calculation.
- Core flows remain readable at larger font sizes and operable by screen reader and keyboard/switch focus.

## Assumptions and open questions

- Initial release is single-device and account-free; the user can clear local data.
- An optional OpenAI-backed Ktor service exists, but it is not deployed; decide service authorization, abuse controls, retention terms, and privacy policy before enabling it for users.
- Product copy and default locale are English for the design phase; date formatting and timezone are localized by platform APIs.
- Decide before implementation whether task deadlines support date-only values only (recommended initially) or exact instants. Do not convert a date-only deadline into a time.
- Define data export/retention expectations before a public release.
