# Implementation roadmap

The project has completed an initial compatibility and UI foundation pass: Compose Material 3 theme and five tabs, a shared/native FocusAI focus mark, an indigo/lilac light/dark palette with apricot accent, Room 3 task/note/settings/session persistence, Koin, shared MVVM state, Ktor with JSON serialization and a typed HTTPS adapter, Inbox search/status filters/edit/archive/restore/delete, local note dates, task create/edit/delete, task search and status filters, completion Undo with branded feedback, persisted manual task order, optional due dates, task checklist steps, editable theme/language/focus preferences, timestamp-based focus controls and recent session history, English/Persian/Spanish interface copy with RTL for Persian, and a sample-only note suggestion/review flow. No remote AI backend is configured.

## Phase 0 — Compatibility spike and app foundation

**Status:** library versions are pinned. Room/KSP generation, schema v1→v2→v3→v4→v5→v6→v7→v8 migration generation, Android debug assembly, and iOS simulator shared-source compilation succeeded. System/Light/Dark theme, English/Persian/Spanish language and focus duration preferences are persisted and editable. Navigation currently uses tab selection state; introduce retained per-tab back stacks when nested flows are implemented.

**Acceptance:** Android and iOS shared app compile; five destinations preserve state; back behavior is defined on both; theme switch renders light/dark/system; no unsupported dependency remains speculative.

## Phase 1 — Domain and local persistence

**Work:** domain types, repository contracts, Room schema/entities/DAOs/mappers, preferences, DI modules, fake clock, note/task/subtask/session storage.

**Acceptance:** local CRUD and Flow observation work offline; atomic selected-suggestion write API exists; migration policy and tests are in place.

## Phase 2 — Core manual productivity flows

**Work:** Today, Inbox, editor, task list/details, planner order, completion/undo and empty/error states.

**Status:** local notes and task CRUD are implemented. Task search and All/Open/Completed filters are available; task edits retain completion and creation metadata. Completion changes offer Undo, active tasks can be reordered through accessible controls saved in Room, and task details support optional validated dates and editable checklist steps. Inbox supports search, review/archive filters, local update dates, archive/restore, and confirmed permanent deletion. Remaining items include note-delete undo and accessibility review.

**Acceptance:** user can capture a note and create/edit/complete/reorder tasks with no network; state is reflected across entry points; accessibility and large-text behavior reviewed.

## Phase 3 — Focus and explainable insights

**Status:** focus sessions now persist start, pause, resume, finish and discard transitions in Room. Elapsed time is derived from saved timestamps, sessions optionally link to open tasks, and recent history survives app recreation. The active session is restored from Room when the app starts. Derived insights, period filters, and timer lifecycle verification on devices remain.

**Work:** persisted timestamp timer transitions, optional task linkage, history, derived metrics and settings/privacy controls.

**Acceptance:** timer survives backgrounding/recreation and uses timestamps; abandon/discard behavior is clear; each metric matches documented records and period semantics.

## Phase 4 — AI contracts and deterministic mock

**Work:** versioned DTOs, validation/normalization, provider abstraction, mock scenarios, extraction/breakdown/planning review state, explicit confirmation.

**Acceptance:** all mock scenarios are deterministic; invalid/partial/duplicate output recovers safely; no proposal persists before selected confirmation; existing subtasks remain untouched.

## Phase 5 — Secure backend integration

**Work:** Ktor remote adapter, backend contract/configuration, error mapping, privacy copy, request limits and observable provider status. Backend service itself is a separate project/deployment decision and is not part of this client repository phase.

**Acceptance:** app contains no provider secret; backend owns credentials and model selection; offline/timeout/rate-limit/retry paths preserve input; only explicit AI action transmits note content.

## Phase 6 — Polish and release readiness

**Work:** visual verification on Android/iOS, accessibility audits, localization/date testing, performance, crash-safe persistence, release build configuration and real version/project metadata.

**Acceptance:** testing gates in `../testing/testing-strategy.md` pass on supported toolchains; docs match shipped behavior; no demo data is presented as user data.

## Dependency order and risks

The selected Room/KSP/Koin/Ktor libraries compile for Android and shared iOS simulator sources with this repository's declared toolchain. Remaining risks include Room schema evolution, retained navigation back-stack parity, Android API 37 availability in CI, and full Xcode app integration. Keep additions in the version catalog and recheck compatibility when upgrading. The repository currently has no Git metadata, so establish version control before implementation work that needs reviewable commits.
