# Implementation roadmap

The project has completed an initial compatibility and UI foundation pass: Compose Material 3 theme and five tabs, a shared/native FocusAI focus mark, an indigo/lilac light/dark palette with apricot accent, Room 3 task/note/settings/session persistence, Koin, shared MVVM state, Ktor with JSON serialization and a typed HTTPS adapter, Inbox search/status filters/edit/archive/restore/delete, local note dates, full-height task create/edit screens with calendar date selection, task search and status filters, completion Undo with branded feedback, persisted manual task order, task checklist steps, editable theme/language/focus preferences, timestamp-based focus controls and recent session history, English/Persian/Spanish/French/Arabic interface copy with RTL for Persian and Arabic, Manrope/Noto Sans Arabic typography, explainable 7/30-day focus insights with task-completion timestamps, and a sample-only note suggestion/review flow. No remote AI backend is configured.

## Phase 0 — Compatibility spike and app foundation

**Status:** library versions are pinned. Room/KSP generation, schema v1→v2→v3→v4→v5→v6→v7→v8→v9 migration generation, Android debug assembly, iOS simulator shared-source compilation, and the full iOS simulator app build succeeded. System/Light/Dark theme, English/Persian/Spanish/French/Arabic language and focus duration preferences are persisted and editable. The app bar stays pinned; secondary task editing and destination changes animate. Native back handling for nested destinations and retained per-tab scroll state remain future polish.

**Acceptance:** Android and iOS shared app compile; five destinations preserve state; back behavior is defined on both; theme switch renders light/dark/system; no unsupported dependency remains speculative.

## Phase 1 — Domain and local persistence

**Work:** domain types, repository contracts, Room schema/entities/DAOs/mappers, preferences, DI modules, fake clock, note/task/subtask/session storage.

**Acceptance:** local CRUD and Flow observation work offline; atomic selected-suggestion write API exists; migration policy and tests are in place.

## Phase 2 — Core manual productivity flows

**Work:** Today, Inbox, editor, task list/details, planner order, completion/undo and empty/error states.

**Status:** local notes and task CRUD are implemented. Task search and All/Open/Completed filters are available; task edits retain completion and creation metadata. Completion changes offer Undo, active tasks can be reordered through accessible controls saved in Room, and the full-height task editor supports calendar-selected dates and editable checklist steps. Inbox supports search, review/archive filters, local update dates, archive/restore, and confirmed permanent deletion with an Undo action that restores the complete Room entity. Screen-reader labels now cover task completion, reorder, edit, archive, restore, and delete actions in all five interface languages. Platform-level touch-target, screen-reader-order, and larger-text checks remain.

**Acceptance:** user can capture a note and create/edit/complete/reorder tasks with no network; state is reflected across entry points; accessibility and large-text behavior reviewed on supported platforms.

## Phase 3 — Focus and explainable insights

**Status:** focus sessions persist start, pause, resume, finish and discard transitions in Room. Elapsed time is derived from saved timestamps, sessions optionally link to open tasks, and recent history survives app recreation. The active session is restored from Room when the app starts. The Focus screen now shows rolling 7/30-day completed focus minutes, session count, average session length, timestamped task completion count, and a seven-bucket completed-session trend. Task completion timestamps are stored from schema v9 onward; pre-v9 completed tasks have no knowable completion time and are excluded with an explanatory note. Device lifecycle verification remains.

**Work:** persisted timestamp timer transitions, optional task linkage, history, derived metrics and settings/privacy controls.

**Acceptance:** timer survives backgrounding/recreation and uses timestamps; abandon/discard behavior is clear; each metric matches documented records and period semantics. Insights use rolling windows of exactly 7×24 or 30×24 hours ending at the current time. Trend buckets divide the selected window into seven equal intervals; task completion counts use `completedAtEpochMillis`, while focus metrics include only sessions whose outcome is `COMPLETED` and whose end timestamp is in the selected window. Focus minutes and average length sum `accumulatedRunningMillis`; discarded and incomplete sessions are excluded.

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

The selected Room/KSP/Koin/Ktor libraries compile for Android and iOS, and the full iOS simulator app target builds. The iOS target reports a bundle identifier validation warning that should be resolved when the product identifier is confirmed for release. Remaining risks include Room schema evolution, retained navigation back-stack parity, Android API 37 availability in CI, release signing and device-level verification. Keep additions in the version catalog and recheck compatibility when upgrading. The repository has Git metadata; current changes are uncommitted.
